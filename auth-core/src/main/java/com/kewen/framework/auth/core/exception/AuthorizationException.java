package com.kewen.framework.auth.core.exception;

/**
 * 权限异常
 * @author kewen
 * @since 2023-12-04
 */
public class AuthorizationException extends  RuntimeException{
    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(Throwable cause) {
        super("授权异常： "+cause.getMessage(),cause);
    }
}
