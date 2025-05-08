package com.kewen.framework.auth.rabc.model;

import lombok.Data;

/**
 * @author kewen
 * @since 2024-05-11
 */
@Data
public class RabcResult<T> {
    private Integer code = 200;
    private Boolean success = true;
    private String message = "成功";
    private T data;
    public static <T> RabcResult<T> success(){
        return success(null);
    }
    public static <T> RabcResult<T> success(T data){
        RabcResult<T> rabcResult = new RabcResult<T>();
        rabcResult.setData(data);
        return rabcResult;
    }
    public static RabcResult failed(String message){
        RabcResult rabcResult = new RabcResult();
        rabcResult.setSuccess(false);
        rabcResult.setCode(500);
        rabcResult.setMessage(message);
        return rabcResult;
    }
    public static RabcResult failed(int code , String message){
        RabcResult rabcResult = new RabcResult();
        rabcResult.setSuccess(false);
        rabcResult.setCode(code);
        rabcResult.setMessage(message);
        return rabcResult;
    }

}
