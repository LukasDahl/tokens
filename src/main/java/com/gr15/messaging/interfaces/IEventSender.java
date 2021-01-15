package com.gr15.messaging.interfaces;

import com.gr15.messaging.models.*;

/**
 * @author Wassim
 */

public interface IEventSender {
    void sendEvent(Event event, String exchangeName, String queueType, String topic) throws Exception;
}
