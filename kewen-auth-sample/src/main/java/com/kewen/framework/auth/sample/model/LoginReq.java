package com.kewen.framework.auth.sample.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author kewen
 * @since 2024-05-11
 */
@Data
public class LoginReq {


    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

}
