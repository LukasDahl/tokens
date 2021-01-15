/**
 * @author Wassim
 */

package com.gr15.Interfaces;

import com.gr15.exceptions.QueueException;
import com.gr15.messaging.models.TokenInfo;


public interface IQueueService {

    TokenInfo validateToken(String token) throws QueueException;

}
