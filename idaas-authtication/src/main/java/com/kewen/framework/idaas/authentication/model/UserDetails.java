package com.kewen.framework.idaas.authentication.model;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 2025/04/11
 *
 * @author kewen
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class UserDetails {
    private String username;
    private String password;
    private String email;
    private String token;
}
