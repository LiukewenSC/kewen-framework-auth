package com.kewen.framework.auth.core.context;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

/**
 * 具有权限的用户上下文，用户登录了之后每次请求都应该有上下文信息
 */
public class CurrentUserAuthContext implements ApplicationContextAware {

    private static CurrentAuthUserContextContainer container ;

    public static Collection<String> getAuths() {
        return container.getAuths();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //必须要保证有一个上下文容器
        CurrentUserAuthContext.container=applicationContext.getBean(CurrentAuthUserContextContainer.class);
    }
}
