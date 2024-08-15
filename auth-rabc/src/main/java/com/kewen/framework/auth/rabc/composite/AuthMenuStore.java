package com.kewen.framework.auth.rabc.composite;


import com.kewen.framework.auth.rabc.mp.entity.SysAuthMenu;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuApi;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuRoute;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 菜单权限存储，可以是内存或redis等
 * @author kewen
 * @since 2024-08-15
 */
public interface AuthMenuStore {

    List<SysMenuApi> getMenuRequests(Callable<List<SysMenuApi>> callable);

    List<SysMenuRoute> getMenuRoutes(Callable<List<SysMenuRoute>> callable);

    List<SysAuthMenu> getAuthMenus(Callable<List<SysAuthMenu>> callable);
}
