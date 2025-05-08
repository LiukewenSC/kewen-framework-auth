package com.kewen.framework.idaas.exception;

/**
 * @descrpition 基础异常
 * @author kewen
 */
public class BackendException extends RuntimeException{

    protected Integer code = 500;

    public BackendException() {
    }

    public BackendException(String message) {
        super(message);
    }

    public BackendException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackendException(Throwable cause) {
        super(cause);
    }

    public BackendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
