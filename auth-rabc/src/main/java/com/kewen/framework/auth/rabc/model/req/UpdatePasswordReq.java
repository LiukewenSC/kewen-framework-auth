package com.kewen.framework.auth.rabc.model.req;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码
 * @author kewen
 * @since 2024-08-22
 */
@Data
public class UpdatePasswordReq {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
