package com.kewen.framework.auth.core.exception;

/**
 * 后台异常
 * @author kewen
 * @since 2024-07-01
 */
public class AuthServiceException extends RuntimeException{
    public AuthServiceException(String message) {
        super(message);
    }

    public AuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
