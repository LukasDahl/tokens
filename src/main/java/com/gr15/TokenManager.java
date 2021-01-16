package com.gr15;

import com.gr15.exceptions.QueueException;
import com.gr15.messaging.models.TokenInfo;
import com.gr15.messaging.rabbitmq.RabbitMqSender;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Lukas Amtoft Dahl
 */

public class TokenManager {

    private static final String SYMBOLS = "ABCDEFGJKLMNPRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 23;
    private static final int TOKEN_WORD_LENGTH = 5;

    private static TokenManager instance = null;

    private final QueueService queueService = new QueueService(new RabbitMqSender());
    private final HashMap<String, LinkedList<String>> tokenMap = new HashMap<>();
    private final HashMap<String, LinkedList<String>> usedTokenMap = new HashMap<>();

    public static TokenManager getInstance() {
        if (instance == null) instance = new TokenManager();
        return instance;
    }

    private TokenManager() {
    }

    public static String tokenBuilder() {
        char[] values = new char[TOKEN_LENGTH];

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            if ((i + 1) % (TOKEN_WORD_LENGTH + 1) == 0)
                values[i] = '-';
            else
                values[i] = SYMBOLS.charAt(new SecureRandom().nextInt(SYMBOLS.length()));
        }

        String token = new String(values);
        System.out.println(token);
        return token;
    }

    public TokenInfo consumeToken(String token) {
        for (String key : tokenMap.keySet()) {
            for (String storedToken : tokenMap.get(key)) {
                if (storedToken.equals(token)) {
                    usedTokenMap.get(key).add(storedToken);
                    tokenMap.get(key).remove(storedToken);
                    return new TokenInfo(storedToken, false, key);
                }
            }
        }
        for (String key : usedTokenMap.keySet()) {
            for (String storedToken : usedTokenMap.get(key)) {
                if (storedToken.equals(token)) {
                    return new TokenInfo(storedToken, true, key);
                }
            }
        }
        return new TokenInfo(token, false, "");
    }

    public boolean accountExists(String accountID) throws QueueException {
        boolean exists;
        if (accountID.split(";").length > 1)
            exists = true;
        else
            exists = queueService.accountExists(accountID);
        System.out.println("Exists in accountExists: " + exists);
        if (exists) {
            if (!tokenMap.containsKey(accountID)) {
                tokenMap.put(accountID, new LinkedList<>());
                usedTokenMap.put(accountID, new LinkedList<>());
            }
        }
        return exists;
    }

    public String[] generateTokens(int count, String accountID) throws BadRequestException {
        String[] stringArray = new String[count];
        if (count > 5) {//Too many tokens requested
            JsonObject response = Json.createObjectBuilder().add("errorMessage", "You cannot request more than 5 tokens").build();
            throw new BadRequestException(Response.status(400).entity(response).type(MediaType.APPLICATION_JSON).build());
        } else if (tokenMap.get(accountID).size() > 1) { //Account has too many tokens
            JsonObject response = Json.createObjectBuilder().add("errorMessage", "Account has more than 1 token").build();
            throw new BadRequestException(Response.status(400).entity(response).type(MediaType.APPLICATION_JSON).build());
        } else { //Success
            for (int i = 0; i < count; i++) {
                String token = TokenManager.tokenBuilder();
                stringArray[i] = token;
                tokenMap.get(accountID).add(token);
            }
        }
        return stringArray;
    }

    public void deleteAccount(String accountID){
        tokenMap.remove(accountID);
        usedTokenMap.remove(accountID);
    }


}
