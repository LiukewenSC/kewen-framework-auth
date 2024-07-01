package com.kewen.framework.auth.core.exception;

/**
 * 后台异常
 * @author kewen
 * @since 2024-07-01
 */
public class BackendException extends RuntimeException{
    public BackendException(String message) {
        super(message);
    }

    public BackendException(String message, Throwable cause) {
        super(message, cause);
    }
}
