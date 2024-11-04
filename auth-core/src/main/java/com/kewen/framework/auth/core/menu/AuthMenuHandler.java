package com.kewen.framework.auth.core.menu;


import com.kewen.framework.auth.core.entity.BaseAuth;
import com.kewen.framework.auth.core.entity.IAuthObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 菜单处理器，处理菜单相关
 * @author kewen
 * @since 2024-11-01
 */
public interface AuthMenuHandler {


    Logger logger = LoggerFactory.getLogger(AuthMenuHandler.class);
    /**
     * 是否有菜单访问权限
     *  对应菜单 范围权限 @AuthCheckMenuAccess
     * @param auths
     * @param path
     * @return
     */
    boolean hasMenuAccessAuth(Collection<BaseAuth> auths, String path) ;
    default boolean hasMenuAccessAuth(IAuthObject authObject, String path) {
        if (authObject==null){
            logger.info("authObject is null");
            return false;
        }
        return hasMenuAccessAuth(authObject.listBaseAuth(), path);
    }
}
