package com.kewen.framework.auth.sample.Response;

import com.kewen.framework.auth.rabc.model.Result;
import com.kewen.framework.auth.security.model.SecurityUser;
import com.kewen.framework.auth.security.response.AuthenticationSuccessResultResolver;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证成功用户转化类，封装返回结构
 * @author kewen
 * @since 2024-07-05
 */
@Component
public class SampleAuthenticationSuccessResultResolver implements AuthenticationSuccessResultResolver {

    @Override
    public Object resolver(HttpServletRequest request, HttpServletResponse response, SecurityUser user) {
        return Result.success(user);
    }
}
