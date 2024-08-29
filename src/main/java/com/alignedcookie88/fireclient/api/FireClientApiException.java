package com.alignedcookie88.fireclient.api;

public class FireClientApiException extends RuntimeException {
    public FireClientApiException(String message) {
        super(message);
    }

    public FireClientApiException(String message, Exception e) {
        super(message, e);
    }
}
