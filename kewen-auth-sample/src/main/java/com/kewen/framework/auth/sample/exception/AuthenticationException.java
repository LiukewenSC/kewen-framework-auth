package com.kewen.framework.auth.sample.exception;

/**
 * 权限异常
 * @author kewen
 * @since 2023-12-04
 */
public class AuthenticationException extends  RuntimeException{
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super("授权异常： "+cause.getMessage(),cause);
    }
}
