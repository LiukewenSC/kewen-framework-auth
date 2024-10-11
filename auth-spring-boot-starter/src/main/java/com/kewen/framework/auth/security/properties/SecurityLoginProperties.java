package com.kewen.framework.auth.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;



/**
 * 默认配置
 * @author kewen
 * @since 2024-07-02
 */
@ConfigurationProperties("kewen.auth.security.login")
@Data
public class SecurityLoginProperties {
    /**
     * 登录的url
     */
    private String loginUrl = "/login";
    /**
     * 用户名参数名
     */
    private String usernameParameter = "username";
    /**
     * 密码参数名
     */
    private String passwordParameter = "password";
    /**
     * 当前用户登录
     */
    private String currentUserUrl = "/currentUser";
}
