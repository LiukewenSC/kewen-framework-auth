package com.kewen.framework.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kewen.framework.auth.rabc.composite.SysUserComposite;
import com.kewen.framework.auth.security.configurer.PermitUrlContainer;
import com.kewen.framework.auth.security.response.AuthenticationSuccessResultResolver;
import com.kewen.framework.auth.security.response.SecurityAuthenticationExceptionResolverHandler;
import com.kewen.framework.auth.security.response.SecurityAuthenticationSuccessHandler;
import com.kewen.framework.auth.security.service.RabcSecurityUserDetailsService;
import com.kewen.framework.auth.security.service.SecurityUserDetailsService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityBeanConfig {


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
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler(
            ObjectProvider<AuthenticationSuccessResultResolver> resultResolverProvider,ObjectMapper objectMapper){
        return new SecurityAuthenticationSuccessHandler(resultResolverProvider,objectMapper);
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
