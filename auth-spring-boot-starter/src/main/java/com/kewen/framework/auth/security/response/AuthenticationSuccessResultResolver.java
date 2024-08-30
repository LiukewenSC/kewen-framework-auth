package com.kewen.framework.auth.security.response;

import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证相关成功用户转化解析器，
 * 用于认证成功返回、登出成功返回、获取当前用户等在security层面不经过业务的数据返回封装
 * @author kewen
 * @since 2024-07-04
 */
public interface AuthenticationSuccessResultResolver {
    /**
     * 处理返回格式
     * @param request  请求
     * @param response 响应，注意不要开关流
     * @param data 准备返回的数据，可以为空
     * @return 准备写流的数据
     */
    Object resolver(HttpServletRequest request, HttpServletResponse response, @Nullable Object data);
}
