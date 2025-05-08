package com.kewen.framework.auth.rabc.composite.impl;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kewen.framework.auth.rabc.composite.AuthMenuStore;
import com.kewen.framework.auth.rabc.mp.entity.SysAuthMenu;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuApi;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuRoute;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *  内存中可过期的菜单存储
 * @author kewen
 * @since 2024-08-15
 */
public class MemoryExpiredAuthMenuStore implements AuthMenuStore {
    /**
     * 菜单、权限的缓存
     */
    private static final Cache<String, Object> cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();


    @Override
    public List<SysMenuApi> getApiMenus(Callable<List<SysMenuApi>> callable) {
        try {
            return  (List<SysMenuApi>) cache.get("getApiMenus", callable);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SysMenuRoute> getRouteMenus(Callable<List<SysMenuRoute>> callable) {
        try {
            return  (List<SysMenuRoute>) cache.get("getRouteMenus", callable);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SysAuthMenu> getAuthMenus(Callable<List<SysAuthMenu>> callable) {
        try {
            return  (List<SysAuthMenu>) cache.get("getAuthMenus", callable);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
