package com.kewen.framework.auth.security.config;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * security配置自定义
 * @author kewen
 * @since 2024-08-27
 */
public interface HttpSecurityCustomizer {
    /**
     * 自定义security相关的参数，会覆盖原来的数据
     * @param http
     */
    void customizer(HttpSecurity http) throws Exception;
}
