package com.kewen.framework.auth.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kewen-framework.security.login")
@Data
public class SecurityLoginProperties {
    /**
     * 登录的url
     */
    private String loginUrl = "/login";
    /**
     * 允许的最大session数量
     */
    private Integer maximumSessions = 1;
    /**
     * 到达最大session阻止登录
     */
    private Boolean maxSessionsPreventsLogin = true;
}
