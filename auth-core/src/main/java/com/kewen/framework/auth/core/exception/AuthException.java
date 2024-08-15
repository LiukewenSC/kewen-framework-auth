package com.kewen.framework.auth.core.exception;

/**
 * 权限异常
 * @author kewen
 * @since 2023-12-04
 */
public class AuthException extends  RuntimeException{
    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super("权限异常： "+cause.getMessage(),cause);
    }
}
