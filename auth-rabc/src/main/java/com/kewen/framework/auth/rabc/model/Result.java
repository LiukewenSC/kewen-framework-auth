package com.kewen.framework.auth.rabc.model;

import lombok.Data;

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
    public static <T> Result<T> success(){
        return success(null);
    }
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<T>();
        result.setData(data);
        return result;
    }
    public static Result failed(String message){
        Result  result = new Result();
        result.setSuccess(false);
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
    public static Result failed(int code , String message){
        Result  result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

}
