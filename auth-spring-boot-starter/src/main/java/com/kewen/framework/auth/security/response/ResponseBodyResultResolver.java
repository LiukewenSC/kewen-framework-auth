package com.kewen.framework.auth.security.response;

import com.kewen.framework.auth.security.model.SecurityUser;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证成功用户转化类，处理用户的额外信息
 * @author kewen
 * @since 2024-07-04
 */
public interface ResponseBodyResultResolver {
    /**
     * 处理返回格式
     * @param request  请求
     * @param response 响应，注意不要开关流
     * @param data 准备返回的数据，可以为空
     * @return 准备写流的数据
     */
    Object resolver(HttpServletRequest request, HttpServletResponse response, @Nullable Object data);
}
