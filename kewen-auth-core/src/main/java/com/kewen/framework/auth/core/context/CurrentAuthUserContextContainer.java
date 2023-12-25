package com.kewen.framework.auth.core.context;

import java.util.Collection;

/**
 * 用户权限上下文容器，用户登录后会将用户信息写入容器中，可以从上下文中获取
 * @descrpition
 * @author kewen
 * @since 2022-11-25
 */
public interface CurrentAuthUserContextContainer {

    /**
     * 获取用户权限
     * @return
     */
    Collection<String> getAuths();

    void setAuths(Collection<String> auths);
}
