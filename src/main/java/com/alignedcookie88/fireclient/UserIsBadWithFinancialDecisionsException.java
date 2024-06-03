package com.alignedcookie88.fireclient;

public class UserIsBadWithFinancialDecisionsException extends RuntimeException {

    String message;

    public UserIsBadWithFinancialDecisionsException(String s) {
        message = s;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
