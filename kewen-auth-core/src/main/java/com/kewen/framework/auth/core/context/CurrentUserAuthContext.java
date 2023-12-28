package com.kewen.framework.auth.core.context;


import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.BaseAuth;

import java.util.Collection;

/**
 * 具有权限的用户上下文，用户登录了之后每次请求都应该有上下文信息
 * 记得使用前应该先登录，然后存储到CurrentAuthUserContextContainer中
 */
public class CurrentUserAuthContext {

    private static UserAuthContextContainer container ;

    public static Collection<BaseAuth> getAuths() {
        return container.getAuths();
    }
    public static IAuthObject getAuthObject(){
        return container.getAuthObject();
    }

    public static void setAuthObject(IAuthObject authObject){
        container.setAuthObject(authObject);
    }


    public void setContainer(UserAuthContextContainer container) {
        CurrentUserAuthContext.container = container;
    }
}
