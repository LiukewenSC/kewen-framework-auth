package com.kewen.framework.auth.sample;

import com.kewen.framework.auth.sample.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author kewen
 * @since 2024-05-11
 */
@RestControllerAdvice
@Slf4j
public class RepAdvance {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(Throwable e){
        log.error(e.getMessage(),e);
        return Result.fail(e.getMessage());

    }
}
