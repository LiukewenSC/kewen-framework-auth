package com.kewen.framework.idaas.model;


import lombok.Data;

/**
 * @descrpition 返回类
 * @author kewen
 */
@Data
public class Result<T> {

    protected Integer code;
    protected Boolean success ;
    protected String message;
    protected T data;

    public static Result<Void> success(){
        Result<Void> result = new Result<>();
        result.code=200;
        result.success=true;
        result.message="成功";
        return result;
    }
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.code=200;
        result.message="成功";
        result.success=true;
        result.data=data;
        return result;
    }
    public static Result<Void> failed(Integer code,String message){
        Result<Void> result = new Result<>();
        result.code=code;
        result.success=false;
        result.message=message;
        return result;
    }
    public static Result<Void> failed(String message){
        Result<Void> result = new Result<>();
        result.code=500;
        result.success=false;
        result.message=message;
        return result;
    }
}
