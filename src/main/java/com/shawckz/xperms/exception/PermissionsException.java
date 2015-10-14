package com.shawckz.xperms.exception;

/**
 * Created by 360 on 9/21/2015.
 */
public class PermissionsException extends RuntimeException {

    public PermissionsException() {
    }

    public PermissionsException(String message) {
        super(message);
    }

    public PermissionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionsException(Throwable cause) {
        super(cause);
    }

    public PermissionsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }



}
