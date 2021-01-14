package com.gr15;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * @author Lukas Amtoft Dahl
 */

@Path("/tokens")
public class TokenResource {

    HashMap<String, LinkedList<String>> tokenMap = new HashMap<>();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonArray createTokens(JsonObject json) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        if (!tokenMap.containsKey(json.getString("id"))) tokenMap.put(json.getString("id"), new LinkedList<>());

        if (json.getInt("count") > 5) {//Too many tokens requested
            JsonObject response = Json.createObjectBuilder().add("errorMessage", "You cannot request more than 5 tokens").build();
            throw new BadRequestException(Response.status(400).entity(response).type(MediaType.APPLICATION_JSON).build());
        }
        else if (tokenMap.get(json.getString("id")).size() > 1) { //Account has too many tokens
            JsonObject response = Json.createObjectBuilder().add("errorMessage", "Account has more than 1 token").build();
            throw new BadRequestException(Response.status(400).entity(response).type(MediaType.APPLICATION_JSON).build());
        }
        else { //Success
            for (int i = 0; i < json.getInt("count"); i++) {
                String token = Token.tokenBuilder();
                arrayBuilder.add(token);
                tokenMap.get(json.getString("id")).add(token);
            }
        }

        return arrayBuilder.build();
    }

    public boolean consumeToken(String token){
        for (LinkedList<String> tokens: tokenMap.values()){
            for (String storedToken: tokens){
                if (storedToken.equals(token)){
                    tokens.remove(storedToken);
                    return true;
                }
            }
        }
        return false;
    }
}