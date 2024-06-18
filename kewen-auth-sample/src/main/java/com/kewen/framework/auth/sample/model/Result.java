package com.kewen.framework.auth.sample.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kewen
 * @since 2024-05-11
 */
@Data
public class Result<T> {
    private Integer code = 200;
    private Boolean success = true;
    private String message = "成功";
    private T data;
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<T>();
        result.setData(data);
        return result;
    }
    public static Result fail(String message){
        Result  result = new Result();
        result.setSuccess(false);
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
    public static Result fail(int code ,String message){
        Result  result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

}
