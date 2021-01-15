package com.gr15.messaging.interfaces;

import com.gr15.messaging.models.Event;

/**
 * @author Wassim
 */

public interface IEventReceiver {
    void receiveEvent(Event event) throws Exception;
}