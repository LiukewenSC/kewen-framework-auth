package com.kewen.framework.auth.core.context;


import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.BaseAuth;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

/**
 * 具有权限的用户上下文，用户登录了之后每次请求都应该有上下文信息
 * 记得使用前应该先登录，然后存储到CurrentAuthUserContextContainer中
 */
public class CurrentUserAuthContext implements ApplicationContextAware {

    private static UserAuthContextContainer container ;

    public static Collection<BaseAuth> getAuths() {
        return container.getAuths();
    }
    public static IAuthObject getAuthObject(){
        return container.getAuthObject();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CurrentUserAuthContext.container = applicationContext.getBean(UserAuthContextContainer.class);
    }
}
