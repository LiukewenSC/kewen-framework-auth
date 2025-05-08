package com.kewen.framework.idaas.authentication.handler;


import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.kewen.idaas.authentication.model.LoginReq;
import com.kewen.idaas.authentication.model.UserDetails;
import com.kewen.idaas.authentication.password.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * 2025/04/13
 * 登录过滤器
 *
 * @author kewen
 * @since 1.0.0
 */
@Component
public class UsernamePasswordLoginAdaptor implements AuthenticationAdaptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean support(HttpServletRequest request) {
        return "/public/user/login".equals(request.getRequestURI());
    }

    @Override
    public UserDetails authentication(HttpServletRequest request) throws IOException {

        ServletInputStream inputStream = request.getInputStream();

        LoginReq loginReq = JSONObject.parseObject(IoUtil.readUtf8(inputStream), LoginReq.class);

        return loginService.login(loginReq);
    }
}
