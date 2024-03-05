package com.kewen.framework.auth.sample.config;

import com.kewen.framework.auth.core.context.CurrentUserAuthContext;
import com.kewen.framework.auth.extension.model.DefaultAuthObject;
import com.kewen.framework.auth.extension.model.Role;
import com.kewen.framework.auth.extension.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    CurrentUserAuthContext currentUserAuthContext;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        DefaultAuthObject object = new DefaultAuthObject();
        object.addUsers(new User(1L,"user1"));
        object.addRoles(new Role(1L,"超级管理员"));
        currentUserAuthContext.setAuthObject(object);
        return true;
    }
}
