package com.kewen.framework.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kewen.framework.auth.rabc.composite.SysUserComposite;
import com.kewen.framework.auth.security.before.BeforeSecurityFilter;
import com.kewen.framework.auth.security.before.WebGlobalExceptionHandlerFilter;
import com.kewen.framework.auth.security.configurer.PermitUrlContainer;
import com.kewen.framework.auth.security.before.BeforeSecurityFilterProxy;
import com.kewen.framework.auth.security.response.DefaultSecurityAuthenticationSuccessHandler;
import com.kewen.framework.auth.security.response.ResponseBodyResultResolver;
import com.kewen.framework.auth.security.response.SecurityAuthenticationExceptionResolverHandler;
import com.kewen.framework.auth.security.response.SecurityAuthenticationSuccessHandler;
import com.kewen.framework.auth.security.service.RabcSecurityUserDetailsService;
import com.kewen.framework.auth.security.service.SecurityUserDetailsService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityBeanConfig {

    /**
     *  执行在SpringSecurity前面的过滤器链
     */
    @Bean
    BeforeSecurityFilterProxy beforeSecurityFilter(ObjectProvider<BeforeSecurityFilter> beforeSecurityFilterProvider){
        return new BeforeSecurityFilterProxy(beforeSecurityFilterProvider);
    }
    /**
     * 在SpringSecurity之前的过滤器，处理最后的逻辑
     */
    @Bean
    WebGlobalExceptionHandlerFilter WebGlobalExceptionHandlerFilter(HandlerExceptionResolver handlerExceptionResolver){
        return new WebGlobalExceptionHandlerFilter(handlerExceptionResolver);
    }


    @Bean
    @ConditionalOnClass(RabcSecurityUserDetailsService.class)
    SecurityUserDetailsService securityUserDetailsService(SysUserComposite sysUserComposite){
        RabcSecurityUserDetailsService service = new RabcSecurityUserDetailsService();
        service.setSysUserComposite(sysUserComposite);
        return service;
    };

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    PasswordEncoder passwordEncoder() {
        // 自己不在Bean中添加这个系统默认也是这个
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    @ConditionalOnMissingBean(SecurityAuthenticationSuccessHandler.class)
    SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler(
            ObjectProvider<ResponseBodyResultResolver> resultResolverProvider, ObjectMapper objectMapper){
        return new DefaultSecurityAuthenticationSuccessHandler(resultResolverProvider,objectMapper);
    }


    @Bean
    SecurityAuthenticationExceptionResolverHandler authenticationCompositeHandler(HandlerExceptionResolver handlerExceptionResolver){
        return new SecurityAuthenticationExceptionResolverHandler()
                .setHandlerExceptionResolver(handlerExceptionResolver);
    }
    @Bean
    PermitUrlContainer permitUrlContainer(){
        return new PermitUrlContainer();
    }

}
