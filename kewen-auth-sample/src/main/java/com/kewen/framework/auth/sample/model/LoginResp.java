package com.kewen.framework.auth.sample.model;

import com.kewen.framework.auth.rabc.model.UserAuthObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kewen
 * @since 2024-05-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResp extends UserAuthObject {
    private String token;
}
