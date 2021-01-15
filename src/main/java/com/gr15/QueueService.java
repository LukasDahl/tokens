
package com.gr15;

import com.google.gson.Gson;

import com.gr15.Interfaces.IQueueService;
import com.gr15.exceptions.QueueException;
import com.gr15.messaging.interfaces.IEventReceiver;
import com.gr15.messaging.interfaces.IEventSender;
import com.gr15.messaging.models.Event;
import com.gr15.messaging.models.TokenInfo;
import com.gr15.messaging.rabbitmq.RabbitMqListener;

import java.util.concurrent.CompletableFuture;

/**
 * @author Wassim
 */
public class QueueService implements IEventReceiver, IQueueService {

    private static final String QUEUE_TYPE = "topic";
    private static final String EXCHANGE_NAME = "tokensExchange";

    private static final String ALL_EVENT_BASE = "#";

    private static final String TOKEN_CMD_BASE = "token.cmds.";
    private static final String TOKEN_EVENT_BASE = "token.events.";
    private static final String ACCOUNT_CMD_BASE = "account.cmds.";
    private static final String ACCOUNT_EVENT_BASE = "account.events.";
    private static final String TRANSACTION_EVENT_BASE = "transaction.events.";

    private static final String VALIDATE_TOKEN_CMD = "validateToken";
    private static final String VALIDATE_ACCOUNT_CMD = "validateAccount";
    private static final String ACCOUNT_EXISTS_CMD = "accountExists";
    // private static final String VALIDATE_ACCOUNTS_CMD = "validateAccounts";

    private static final String TOKEN_VALIDATED_EVENT = "tokenValidated";
    private static final String ACCOUNT_VALIDATED_EVENT = "accountValidated";
    private static final String ACCOUNT_EXISTS_EVENT = "accountExists";
    // private static final String ACCOUNTS_VALIDATED_EVENT = "accountsValidated";
    private static final String TRANSACTION_CREATED_EVENT = "transactionCreated";

    private IEventSender eventSender;
    private CompletableFuture<TokenInfo> tokenInfoResult;
    private CompletableFuture<Boolean> accountExistsResult;

    public QueueService(IEventSender eventSender) {
        this.eventSender = eventSender;
        RabbitMqListener r = new RabbitMqListener(this);
        try {
            r.listen(EXCHANGE_NAME, QUEUE_TYPE, ALL_EVENT_BASE);
        } catch (Exception e) {
            throw new Error(e);
        }
    }


    public boolean accountExists(String accountID) throws QueueException {
        Event event = new Event(ACCOUNT_EXISTS_CMD, accountID);
        accountExistsResult = new CompletableFuture<Boolean>();
        try {
            eventSender.sendEvent(event, EXCHANGE_NAME, QUEUE_TYPE, ACCOUNT_CMD_BASE + ACCOUNT_EXISTS_CMD);
        } catch (Exception e) {
            throw new QueueException("Error while validating account");
        }

        return accountExistsResult.join();
    }


    @Override
    public void receiveEvent(Event event) throws QueueException {

        System.out.println("Handling event: " + event);

        if (event.getEventType().equals(VALIDATE_TOKEN_CMD)){

            TokenManager tokenManager = TokenManager.getInstance();

            var token = new Gson().fromJson(new Gson().toJson(event.getEventInfo()), String.class);

            Event response;
            String account = tokenManager.consumeToken(token);
            if (account.equals("Token not found")){
                String reply = account;
                response = new Event(TOKEN_VALIDATED_EVENT, reply);
            }
            else {
                TokenInfo reply = new TokenInfo(token, account);
                response = new Event(TOKEN_VALIDATED_EVENT, reply);
            }

            try {
                eventSender.sendEvent(response, EXCHANGE_NAME, QUEUE_TYPE, TOKEN_EVENT_BASE + TOKEN_VALIDATED_EVENT);
            } catch (Exception e) {
                throw new QueueException("Error while returning token");
            }
        }
        else if (event.getEventType().equals(ACCOUNT_EXISTS_EVENT)) {

            var accountExists = new Gson().fromJson(new Gson().toJson(event.getEventInfo()), Boolean.class);

            accountExistsResult.complete(accountExists);

        } else {
            System.out.println("event ignored: " + event);
        }
    }

}
