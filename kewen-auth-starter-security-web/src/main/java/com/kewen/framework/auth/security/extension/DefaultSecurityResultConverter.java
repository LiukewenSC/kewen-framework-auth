package com.kewen.framework.auth.security.extension;

import lombok.Data;

public class DefaultSecurityResultConverter implements SecurityResultConverter{

    @Override
    public Object convertSuccess(Object data){
        SecurityResult result = new SecurityResult();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }
    @Override
    public Object convertFailed(Integer code,Throwable t){
        SecurityResult result = new SecurityResult();
        result.setCode(code);
        result.setMessage("失败");
        result.setData(t.getMessage());
        return result;
    }
    @Data
    public static class SecurityResult{
        private Integer code;
        private String message;
        private Object data;
    }

}
