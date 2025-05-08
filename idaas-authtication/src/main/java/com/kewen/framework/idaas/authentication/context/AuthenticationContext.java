package com.kewen.framework.idaas.authentication.context;


import com.kewen.idaas.authentication.model.UserDetails;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 2025/04/13
 *
 * @author kewen
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class AuthenticationContext {

    private UserDetails userDetails;

}
