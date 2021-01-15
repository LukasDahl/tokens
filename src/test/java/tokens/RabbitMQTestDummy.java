
package tokens;

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
public class RabbitMQTestDummy implements IEventReceiver {

    private static final String QUEUE_TYPE = "topic";
    private static final String EXCHANGE_NAME = "tokensExchange";

    private static final String ALL_EVENT_BASE = "#";

    //private static final String TOKEN_CMD_BASE = "token.cmds.";
    private static final String TOKEN_EVENT_BASE = "token.events.";
    private static final String ACCOUNT_CMD_BASE = "account.cmds.";
    private static final String ACCOUNT_EVENT_BASE = "account.events.";
    // private static final String TRANSACTION_EVENT_BASE = "transaction.events.";

    private static final String VALIDATE_TOKEN_CMD = "validateToken";
    // private static final String VALIDATE_ACCOUNT_CMD = "validateAccount";
    private static final String ACCOUNT_EXISTS_CMD = "accountExistsRequest";
    // private static final String VALIDATE_ACCOUNTS_CMD = "validateAccounts";

    private static final String TOKEN_VALIDATED_EVENT = "tokenValidated";
    // private static final String ACCOUNT_VALIDATED_EVENT = "accountValidated";
    private static final String ACCOUNT_EXISTS_EVENT = "accountExistsResponse";
    // private static final String ACCOUNTS_VALIDATED_EVENT = "accountsValidated";
    // private static final String TRANSACTION_CREATED_EVENT = "transactionCreated";

    private final IEventSender eventSender;
    private CompletableFuture<Boolean> accountExistsResult;

    public RabbitMQTestDummy(IEventSender eventSender) {
        this.eventSender = eventSender;
        RabbitMqListener r = new RabbitMqListener(this);
        try {
            r.listen(EXCHANGE_NAME, QUEUE_TYPE, ALL_EVENT_BASE);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    public void receiveEvent(Event event) throws QueueException {

        System.out.println("Handling event: " + event);

        if (event.getEventType().equals(ACCOUNT_EXISTS_CMD)){

            var account = new Gson().fromJson(new Gson().toJson(event.getEventInfo()), String.class);

            if (!account.split(";")[1].equals("test"))
                return;

            Event response;

            response = new Event(TOKEN_VALIDATED_EVENT, account + ",1");

            try {
                eventSender.sendEvent(response, EXCHANGE_NAME, QUEUE_TYPE, ACCOUNT_EVENT_BASE + ACCOUNT_EXISTS_EVENT);
            } catch (Exception e) {
                throw new QueueException("Error while validating account");
            }
        } else {
            System.out.println("event ignored: " + event);
        }
    }

}
