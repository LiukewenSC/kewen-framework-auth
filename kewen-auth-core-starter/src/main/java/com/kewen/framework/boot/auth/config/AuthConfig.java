package com.kewen.framework.boot.auth.config;

import com.kewen.framework.auth.core.annotation.menu.MenuAccessInterceptor;
import com.kewen.framework.auth.core.context.CurrentUserAuthContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
@Configuration
@ComponentScan(basePackages = {"com.kewen.framework.auth.core"})
public class AuthConfig implements WebMvcConfigurer {


    /**
     * 创建用户上下文
     * @return
     */
    @Bean
    CurrentUserAuthContext currentUserAuthContext() {
        return new CurrentUserAuthContext();
    }

    @Autowired
    MenuAccessInterceptor menuAccessInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(menuAccessInterceptor);
    }
}
