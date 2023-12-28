package com.kewen.framework.auth.support;

import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.context.UserAuthContextContainer;
import com.kewen.framework.auth.extension.model.DefaultAuthObject;

import java.util.Collection;

/**
 * 默认的当前用户权限存储上下文，这里存储模式可以选择 存入分布式缓存等，暂时不扩展这里，保存到ThreadLocal，以后有需要再扩展
 * @author kewen
 * @since 2023-12-28
 */
public class ThreadLocalUserAuthContextContainer<T extends DefaultAuthObject> implements UserAuthContextContainer<T> {


    private static final ThreadLocal<Object> AUTH_OBJECT_THREAD_LOCAL = new InheritableThreadLocal<>();

    @Override
    public Collection<BaseAuth> getAuths() {
        return ((T) AUTH_OBJECT_THREAD_LOCAL.get()).listBaseAuth();
    }

    @Override
    public void setAuthObject(T authObject) {
        AUTH_OBJECT_THREAD_LOCAL.set(authObject);
    }

    @Override
    public T getAuthObject() {
        return ((T) AUTH_OBJECT_THREAD_LOCAL.get());
    }
}
