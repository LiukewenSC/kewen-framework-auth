package com.kewen.framework.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kewen.framework.auth.security.configurer.JsonLoginAuthenticationFilterConfigurer;
import com.kewen.framework.auth.security.configurer.PermitUrlContainer;
import com.kewen.framework.auth.security.filter.TokenSessionRequestFilter;
import com.kewen.framework.auth.security.response.ResponseBodyResultResolver;
import com.kewen.framework.auth.security.response.SecurityAuthenticationExceptionResolverHandler;
import com.kewen.framework.auth.security.filter.JsonLoginFilter;
import com.kewen.framework.auth.security.properties.SecurityLoginProperties;
import com.kewen.framework.auth.security.response.SecurityAuthenticationSuccessHandler;
import com.kewen.framework.auth.security.service.SecurityUserDetailsService;
import com.kewen.framework.auth.security.filter.AuthUserContextFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
    SecurityUserDetailsService securityUserDetailsService;

    @Autowired
    SecurityAuthenticationSuccessHandler successHandler;

    @Autowired
    SecurityAuthenticationExceptionResolverHandler exceptionResolverHandler;

    @Autowired
    PermitUrlContainer permitUrlContainer;

    @Autowired
    ObjectProvider<ResponseBodyResultResolver> resultResolverProvider ;

    @Autowired
    ObjectMapper objectMapper;
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
        auth.userDetailsService(securityUserDetailsService);
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .regexMatchers(permitUrlContainer.getPermitUrls()).permitAll()
                    .anyRequest().authenticated()
                    .and()
                //.formLogin()  不再用表单登录了，采用Json登录方式，因此不需要再formLogin引入FormLoginConfigurer配置UsernamePasswordAuthenticationFilter
                .apply(new JsonLoginAuthenticationFilterConfigurer<>())
                    .loginProcessingUrl(loginProperties.getLoginUrl())
                    .usernameParameter(loginProperties.getUsernameParameter())
                    .passwordParameter(loginProperties.getPasswordParameter())
                    //.authenticationDetailsSource()  在认证前封装的Authentication中添加详细信息，如从request中拿到的ip,等信息
                    .successHandler(successHandler)
                    .failureHandler(exceptionResolverHandler)
                    .and()
                .logout()
                    .logoutSuccessHandler(successHandler)
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(exceptionResolverHandler)
                    .authenticationEntryPoint(exceptionResolverHandler)
                    .and()
                .sessionManagement()
                //.sessionFixation().changeSessionId()  //这里改为none则不会出现postman连续登录会报session过多 ， 要么就是允许挤下线
                .sessionFixation().none()  //这里改为none则不会出现postman连续登录会报session过多
                    .maximumSessions(loginProperties.getMaximumSessions())
                    .maxSessionsPreventsLogin(loginProperties.getMaxSessionsPreventsLogin()).and()
                    .and()
                .csrf().disable()
                //.cors().configurationSource(corsConfigurationSource()).and()
                //这里添加一个处于最顶端的异常处理过滤器，用来处理默认拦截器未到时发生的异常，
                // 默认的过滤器一般在倒数第二个处理，前面十多个过滤器中的异常都不会捕获
                // 如果不添加此过滤器，则会出现在默认过滤器之前的异常(如用户更新密码等未包含账号密码错误的异常)都不会被捕获，导致重定向到/error（而前后端异常是没有这个页面的），出现循环异常
                // todo但是这里为啥加了ExceptionTranslationFilter就出现了两个了,会把原来的提前至WebAsyncManagerIntegrationFilter之前，又新建一个。最后还是不在这里添加了，放在SpringSecurity过滤器链之前去统一处理
                //.addFilterBefore(new ExceptionTranslationFilter(exceptionResolverHandler), WebAsyncManagerIntegrationFilter.class)
                // 这里就是封装一层request以便获取session
                .addFilterBefore(new TokenSessionRequestFilter(loginProperties.getTokenParameter()), WebAsyncManagerIntegrationFilter.class)
                //这里添加权限用户上下文过滤器
                .addFilterAfter(new AuthUserContextFilter(loginProperties.getCurrentUserUrl(),resultResolverProvider,objectMapper), JsonLoginFilter.class)
        ;
    }

    /**
     * 跨域相关
     * @return
     */
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
