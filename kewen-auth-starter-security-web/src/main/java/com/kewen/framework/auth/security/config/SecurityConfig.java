package com.kewen.framework.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kewen.framework.auth.security.extension.DefaultSecurityResultConverter;
import com.kewen.framework.auth.security.extension.SecurityResultConverter;
import com.kewen.framework.auth.security.filter.response.AuthResultCompositeHandler;
import com.kewen.framework.auth.security.configurer.JsonLoginAuthenticationFilterConfigurer;
import com.kewen.framework.auth.security.filter.response.AuthResultSuccessFailedDeniedHandler;
import com.kewen.framework.auth.security.filter.JsonLoginFilter;
import com.kewen.framework.auth.security.properties.SecurityLoginProperties;
import com.kewen.framework.auth.security.service.RabcSecurityUserDetailsService;
import com.kewen.framework.auth.security.service.SecurityUserDetailsService;
import com.kewen.framework.auth.security.filter.AuthUserContextFilter;
import com.kewen.framework.auth.security.filter.TokenSessionRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author kewen
 * @descrpition
 * @since 2023-02-23
 */
@Configuration
@EnableConfigurationProperties({SecurityLoginProperties.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityLoginProperties loginProperties;

    @Autowired
    ObjectMapper objectMapper;


    @Bean
    @ConditionalOnMissingBean(SecurityResultConverter.class)
    SecurityResultConverter securityResultConverter(){
        return new DefaultSecurityResultConverter();
    }

    @Bean
    @ConditionalOnMissingBean(AuthResultCompositeHandler.class)
    AuthResultCompositeHandler authenticationCompositeHandler(){
        return new AuthResultSuccessFailedDeniedHandler();
    }

    @Autowired
    AuthResultCompositeHandler authResultCompositeHandler;


    @Bean
    @ConditionalOnClass(RabcSecurityUserDetailsService.class)
    SecurityUserDetailsService securityUserDetailsService(){
        return new RabcSecurityUserDetailsService();
    };

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
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
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserDetailsService());
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().authenticated().and()
                //.formLogin()  不再用表单登录了，采用Json登录方式，因此不需要再formLogin引入FormLoginConfigurer配置UsernamePasswordAuthenticationFilter
                .apply(new JsonLoginAuthenticationFilterConfigurer<>())
                    .loginProcessingUrl(loginProperties.getLoginUrl())
                    .usernameParameter(loginProperties.getUsernameParameter())
                    .passwordParameter(loginProperties.getPasswordParameter())
                    //.authenticationDetailsSource()  在认证前封装的Authentication中添加详细信息，如从request中拿到的ip,等信息
                    .successHandler(authResultCompositeHandler)
                    .failureHandler(authResultCompositeHandler)
                    .and()
                .exceptionHandling().accessDeniedHandler(authResultCompositeHandler).and()
                .sessionManagement()
                //.sessionFixation().changeSessionId()  //这里改为none则不会出现postman连续登录会报session过多 ， 要么就是允许挤下线
                .sessionFixation().none()  //这里改为none则不会出现postman连续登录会报session过多
                    .maximumSessions(loginProperties.getMaximumSessions())
                    .maxSessionsPreventsLogin(loginProperties.getMaxSessionsPreventsLogin()).and()
                    .and()
                .csrf().disable()
                //.cors().configurationSource(corsConfigurationSource()).and()
                // 这里就是封装一层request以便获取session
                .addFilterBefore(new TokenSessionRequestFilter(), WebAsyncManagerIntegrationFilter.class)
                //这里添加权限用户上下文过滤器
                .addFilterAfter(new AuthUserContextFilter(), JsonLoginFilter.class)
        ;
    }

    private CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return corsConfigurationSource;
    }

}