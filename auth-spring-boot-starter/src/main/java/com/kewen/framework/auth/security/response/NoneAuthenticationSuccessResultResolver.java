package com.kewen.framework.auth.security.response;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  默认的不做任何转换的认证相关成功用户转化解析器，
 * @author kewen
 * @since 2024-08-30
 */
public class NoneAuthenticationSuccessResultResolver implements AuthenticationSuccessResultResolver{
    private static final Logger log = LoggerFactory.getLogger(NoneAuthenticationSuccessResultResolver.class);

    public NoneAuthenticationSuccessResultResolver() {
        log.info("使用默认的认证相关成功用户转化类，不对认证成功之后的信息做转换");
    }

    @Override
    public Object resolver(HttpServletRequest request, HttpServletResponse response, Object data) {
        return data;
    }
}
