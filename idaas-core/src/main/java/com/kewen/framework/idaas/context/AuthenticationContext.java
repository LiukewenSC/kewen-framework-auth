package com.kewen.framework.idaas.context;


import lombok.Data;

/**
 * 2025/04/11
 *
 * @author kewen
 * @since 1.0.0
 */
@Data
public class AuthenticationContext {
    private String username;
    private String password;
    private String token;
}
