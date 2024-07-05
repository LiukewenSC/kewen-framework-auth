package com.kewen.framework.auth.security.response;

import com.kewen.framework.auth.security.model.SecurityUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证成功用户转化类，处理用户的额外信息
 * @author kewen
 * @since 2024-07-04
 */
public interface AuthenticationSuccessResultResolver {
    /**
     * 处理用户的额外信息如用户删除一些字段，或者是添加额外的返回信息等,封装统一返回结构等
     * @param request  请求
     * @param response 响应，注意不要开关流
     * @param user 认证成功的用户信息
     * @return 准备写流的数据
     */
    Object resolver(HttpServletRequest request, HttpServletResponse response, SecurityUser user);
}
