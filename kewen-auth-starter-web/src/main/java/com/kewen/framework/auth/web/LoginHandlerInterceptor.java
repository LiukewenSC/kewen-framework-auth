package com.kewen.framework.auth.web;

import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.core.exception.AuthorizationException;
import com.kewen.framework.auth.rabc.composite.SysUserComposite;
import com.kewen.framework.auth.rabc.model.UserAuthObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {


    @Autowired
    SysUserComposite sysUserComposite;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");

        UserAuthObject user = TokenUserStore.get(token);

        if (user == null) {
            throw new AuthorizationException("尚未登录，请登录");
        }

        AuthUserContext.setAuthObject(user.getAuthObject());
        return true;
    }
}
