package com.kewen.framework.idaas.authentication.store;


import com.kewen.idaas.authentication.model.LoginReq;
import com.kewen.idaas.authentication.model.UserDetails;

/**
 * 2025/04/11
 *
 * @author kewen
 * @since 1.0.0
 */
public interface AuthenticationStore {
    UserDetails getUserDetails(String token);
    void saveUserDetails(UserDetails userDetails);
    String generateToken(LoginReq loginReq);
}
