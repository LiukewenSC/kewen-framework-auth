package com.kewen.framework.auth.security.filter;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author kewen
 * @descrpition json登录的配置 , 继承AbstractAuthenticationFilterConfigurer 完成自定义的登录过滤器的配置
 * @since 2023-03-01
 */
public class JsonLoginAuthenticationFilterConfigurer<B extends HttpSecurityBuilder<B>>
        extends AbstractAuthenticationFilterConfigurer<B, JsonLoginAuthenticationFilterConfigurer<B>, JsonLoginFilter> {

    public JsonLoginAuthenticationFilterConfigurer() {
        super(new JsonLoginFilter(), null);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    @Override
    protected JsonLoginAuthenticationFilterConfigurer<B> loginPage(String loginPage) {
        throw new RuntimeException("启动失败，不支持配置loginPage");
    }
    public JsonLoginAuthenticationFilterConfigurer<B> usernameParameter(String usernameParameter) {
        getAuthenticationFilter().setUsernameParameter(usernameParameter);
        return this;
    }
    public JsonLoginAuthenticationFilterConfigurer<B> passwordParameter(String passwordParameter) {
        getAuthenticationFilter().setPasswordParameter(passwordParameter);
        return this;
    }
}
