/**
 * @author Wassim
 */

package com.gr15.exceptions;

public class TokenAlreadyUsed extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TokenAlreadyUsed(String message) {
        super(message);
    }
}
