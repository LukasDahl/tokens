package com.gr15.messaging.models;

/**
 * @author Wassim
 */

public class TokenInfo {
    public String Token;
    public String CustomerId;

    public TokenInfo(String token, String customerId) {
        Token = token;
        CustomerId = customerId;
    }
}
