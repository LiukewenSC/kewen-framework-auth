package com.kewen.framework.auth.core.context;


import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.core.model.BaseAuth;

import java.util.Collection;

/**
 * 具有权限的用户上下文，用户登录了之后每次请求都应该有上下文信息
 * 记得使用前应该先登录，然后存储到CurrentAuthUserContextContainer中
 */
public class AuthUserContext {

    private static final ThreadLocal<IAuthObject> AUTH_OBJECT_LOCAL = new InheritableThreadLocal<>();

    public static Collection<BaseAuth> getAuths() {
        return AUTH_OBJECT_LOCAL.get().listBaseAuth();
    }
    public static void setAuthObject(IAuthObject authObject) {
        AUTH_OBJECT_LOCAL.set(authObject);
    }
}
