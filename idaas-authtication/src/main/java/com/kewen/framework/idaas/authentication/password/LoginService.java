package com.kewen.framework.idaas.authentication.password;


import cn.hutool.core.util.IdUtil;
import com.kewen.idaas.authentication.model.LoginReq;
import com.kewen.idaas.authentication.model.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 2025/04/13
 *
 * @author kewen
 * @since 1.0.0
 */
@Component
public class LoginService {

    public UserDetails login(LoginReq loginReq) {
        if (!"kewen".equals(loginReq.getUsername()) || !"123456".equals(loginReq.getPassword())) {
            return null;
        }
        UserDetails userDetails = new UserDetails()
                .setUsername(loginReq.getUsername())
                .setPassword(loginReq.getPassword())
                .setEmail("kewen@kewen.com");
        userDetails.setToken(IdUtil.fastSimpleUUID());
        return userDetails;
    }
}
