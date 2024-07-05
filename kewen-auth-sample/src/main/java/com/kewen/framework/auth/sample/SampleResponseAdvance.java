package com.kewen.framework.auth.sample;

import com.kewen.framework.auth.core.exception.AuthorizationException;
import com.kewen.framework.auth.rabc.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 可以继承 ResponseEntityExceptionHandler，继承了之后会多出来默认的异常解析处理
 * @author kewen
 * @since 2024-05-11
 */
@RestControllerAdvice
@Slf4j
//public class SampleRepopseAdvance extends ResponseEntityExceptionHandler {
public class SampleResponseAdvance {

    @ExceptionHandler(AuthorizationException.class)
    public Result exception(AuthorizationException e){
        log.error(e.getMessage(),e);
        return Result.fail(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public Result accessDeniedException(AccessDeniedException e){
        log.error(e.getMessage(),e);
        return Result.fail(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(Throwable e){
        log.error("全局异常拦截Throwable： "+e.getMessage(),e);
        return Result.fail(e.getMessage());
    }
}
