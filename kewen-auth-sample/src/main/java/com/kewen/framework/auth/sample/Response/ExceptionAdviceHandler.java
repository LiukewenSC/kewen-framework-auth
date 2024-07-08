package com.kewen.framework.auth.sample.Response;

import com.kewen.framework.auth.rabc.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdviceHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdviceHandler.class);

    @ExceptionHandler(Throwable.class)
    public Result throwException(Throwable t){
        logger.error("全局异常：{}", t.getMessage(), t);
        return Result.failed(500, t.getMessage());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public Result accessDeniedException(Throwable t){
        logger.error("访问异常：{}", t.getMessage(), t);
        return Result.failed(401, t.getMessage());
    }
    @ExceptionHandler(AuthenticationException.class)
    public Result authenticationException(Throwable t){
        logger.error("授权异常：{}", t.getMessage(), t);
        return Result.failed(401, t.getMessage());
    }
}
