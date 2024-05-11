package com.kewen.framework.auth.sample.config;

import com.kewen.framework.auth.exception.AuthException;
import com.kewen.framework.auth.sample.model.TokenUserStore;
import com.kewen.framework.auth.support.SimpleThreadLocalUserAuthContextContainer;
import com.kewen.framework.auth.sys.composite.SysUserComposite;
import com.kewen.framework.auth.sys.model.UserAuthObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    SimpleThreadLocalUserAuthContextContainer userAuthContextContainer;

    @Autowired
    SysUserComposite sysUserComposite;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");

        UserAuthObject user = TokenUserStore.get(token);

        if (user == null) {
            throw new AuthException("尚未登录，请登录");
        }


        userAuthContextContainer.setAuthObject(user.getAuthObject());
        return true;
    }
}
