package com.kewen.framework.auth.sample.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kewen.framework.auth.rabc.model.UserAuthObject;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author kewen
 * @since 2024-05-11
 */
public class TokenUserStore {

    private static Cache<String, UserAuthObject> cache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.HOURS).build();

    public static void set(String token, UserAuthObject user){
        cache.put(token, user);
    }
    public static UserAuthObject get(String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        return cache.getIfPresent(token);
    }
}
