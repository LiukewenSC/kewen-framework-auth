package com.kewen.framework.auth.core.context;


import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.core.model.BaseAuth;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * 具有权限的用户上下文，用户登录了之后每次请求都应该有上下文信息
 * 记得使用前应该先登录，然后存储到CurrentAuthUserContextContainer中
 */
public class AuthUserContext {
    private static final ThreadLocal<CurrentUser> CURRENT_USER_LOCAL = new InheritableThreadLocal<>();
    public static Collection<BaseAuth> getAuths() {
        return Optional.ofNullable(getCurrentUser())
                .map(CurrentUser::getAuthObject)
                .map(IAuthObject::listBaseAuth)
                .orElse(Collections.emptyList());
    }
    public static void setCurrentUser(CurrentUser currentUser) {
        CURRENT_USER_LOCAL.set(currentUser);
    }
    public static CurrentUser getCurrentUser() {
        return CURRENT_USER_LOCAL.get();
    }

}
