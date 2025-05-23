package com.kewen.framework.auth.rabc;

import com.kewen.framework.auth.core.menu.AuthMenuHandler;
import com.kewen.framework.auth.core.entity.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import lombok.Setter;

import java.util.Collection;

/**
 * 默认的注解权限处理器的实现，根据数据库来处理
 *
 * @author kewen
 * @since 2023-12-28
 */
@Setter
public class RabcAnnotationAuthHandler implements AuthMenuHandler {

    SysAuthMenuComposite sysAuthMenuComposite;


    @Override
    public boolean hasMenuAccessAuth(Collection<BaseAuth> auths, String path) {
        return sysAuthMenuComposite.hasMenuAuth(auths, path);
    }
}
