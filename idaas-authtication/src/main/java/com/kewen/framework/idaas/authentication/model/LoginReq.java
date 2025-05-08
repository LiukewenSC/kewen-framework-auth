package com.kewen.framework.idaas.authentication.model;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 2025/04/11
 *
 * @author kewen
 * @since 1.0.0
 */
@Data
public class LoginReq {
    @NotNull
    protected String username;
    @NotNull
    protected String password;
}
