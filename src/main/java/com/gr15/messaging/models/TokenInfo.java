package com.gr15.messaging.models;

/**
 * @author Wassim
 */

public class TokenInfo {
    private String token;
    private Boolean isUsed;
    private String customerId;

    public TokenInfo(String token, Boolean isUsed, String customerId) {
        this.token = token;
        this.isUsed = isUsed;
        this.customerId = customerId;
    }

    public String getToken() {
        return this.token;
    }

    public Boolean getIsUsed() {
        return this.isUsed;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}


