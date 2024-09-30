package com.alignedcookie88.fireclient.api;

/**
 * Like FireClientApiException, but won't be treated as the application's fault.
 */
public abstract class FireClientApiUserFaultException extends FireClientApiException {

    public FireClientApiUserFaultException(String message) {
        super(message);
    }

}
