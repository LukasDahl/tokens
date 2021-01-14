package com.gr15;

import java.security.SecureRandom;

/**
 * @author Lukas Amtoft Dahl
 */

public class Token {

    private static final String SYMBOLS = "ABCDEFGJKLMNPRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 23;
    private static final int TOKEN_WORD_LENGTH = 5;

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
}
