package com.kewen.framework.auth.web.config;

import com.kewen.framework.auth.web.LoginHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.kewen.framework.auth.web.controller")
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${kewen-framework.security.login.login-url}")
    private String loginUrl ="/login";

    @Value("${kewen-framework.security.login.token-parameter}")
    private String tokenParameter = "token";

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginHandlerInterceptor loginHandlerInterceptor() {
        LoginHandlerInterceptor interceptor = new LoginHandlerInterceptor();
        interceptor.setTokenParameter(tokenParameter);
        return interceptor;
    }

    public WebConfig() {
        log.info("WebSampleConfig init");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginHandlerInterceptor()).excludePathPatterns(loginUrl);
    }
}
