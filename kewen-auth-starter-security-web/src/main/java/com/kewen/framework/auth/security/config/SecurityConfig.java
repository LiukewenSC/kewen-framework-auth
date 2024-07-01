package com.kewen.framework.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kewen.framework.auth.security.service.SecurityUser;
import com.kewen.framework.auth.security.service.SecurityUserDetailsService;
import com.kewen.framework.auth.security.token.TokenSessionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.io.PrintWriter;

/**
 * @author kewen
 * @descrpition
 * @since 2023-02-23
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    SecurityUserDetailsService securityUserDetailsService(){
        return new SecurityUserDetailsService();
    };

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 加入监听器，session销毁时才会触发 spring容器的感知，否则 security监听不到销毁
     * @return
     */
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //不拦截静态文件
        web.ignoring().antMatchers("/static/**", "/js/**", "/css/**", "/image/**", "/**/**.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().authenticated().and()
                .formLogin()
                .successHandler((request, response, authentication) -> {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof SecurityUser){
                        SecurityUser user = (SecurityUser) principal;
                        user.setToken(request.getSession().getId());
                    }
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(principal));
                    writer.flush();
                    writer.close();
                }).failureHandler((request, response, e) -> {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(e.getMessage()));
                    writer.flush();
                    writer.close();
                })
                .and()
                .exceptionHandling().accessDeniedHandler((request, response, e) -> {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(e.getMessage()));
                    writer.flush();
                    writer.close();
                }).and()
                .sessionManagement()
                //.sessionFixation().changeSessionId()  //这里改为none则不会出现postman连续登录会报session过多
                .sessionFixation().none()  //这里改为none则不会出现postman连续登录会报session过多
                    .maximumSessions(2)
                    .maxSessionsPreventsLogin(true).and()
                    .and()
                .csrf().disable()
                .addFilterBefore(new TokenSessionFilter(), WebAsyncManagerIntegrationFilter.class)
        ;
    }

}
