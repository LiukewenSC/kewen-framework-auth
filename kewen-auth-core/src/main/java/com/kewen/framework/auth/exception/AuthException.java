package com.kewen.framework.auth.exception;

/**
 * 权限异常
 * @author kewen
 * @since 2023-12-04
 */
public class AuthException extends  RuntimeException{
    public AuthException() {
    }

    public AuthException(String message) {
        super("权限异常： "+message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
