package com.kewen.framework.idaas.authentication.store;


import cn.hutool.core.util.IdUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kewen.framework.idaas.authentication.model.LoginReq;
import com.kewen.framework.idaas.authentication.model.UserDetails;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 2025/04/13
 *
 * @author kewen
 * @since 1.0.0
 */
@Component
public class LocalAuthenticationStore implements AuthenticationStore {
    Cache<String , UserDetails> cache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();

    @Override
    public UserDetails getUserDetails(String token) {
        return cache.getIfPresent(token);
    }

    @Override
    public void saveUserDetails( UserDetails userDetails) {
        cache.put(userDetails.getToken(), userDetails);
    }

    @Override
    public String generateToken(LoginReq loginReq) {
        return IdUtil.fastSimpleUUID();
    }
}
