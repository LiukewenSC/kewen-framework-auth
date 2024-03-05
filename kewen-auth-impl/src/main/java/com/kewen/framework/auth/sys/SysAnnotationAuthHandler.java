package com.kewen.framework.auth.sys;

import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;
import com.kewen.framework.auth.sys.composite.SysDataAuthComposite;
import com.kewen.framework.auth.sys.composite.SysMenuAuthComposite;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

/**
 * 默认的注解权限处理器的实现，根据数据库来处理
 * @author kewen
 * @since 2023-12-28
 */
public class SysAnnotationAuthHandler implements AnnotationAuthHandler<Long> {


    SysMenuAuthComposite sysMenuAuthComposite;

    SysDataAuthComposite sysDataAuthComposite;

    @Override
    public DataRangeDatabaseField getDataRangeDatabaseField() {
        return new DataRangeDatabaseField();
    }

    @Override
    public boolean hasMenuAccessAuth(Collection<BaseAuth> auths, String url) {
        return sysMenuAuthComposite.hasMenuAuth(auths, url);
    }

    @Override
    public boolean hasDataOperateAuths(Collection<BaseAuth> auths, String module, String operate, Long dataId) {
        return sysDataAuthComposite.hasDataAuth(auths, module, operate, dataId);
    }

    @Override
    public void editDataAuths(Long dataId, String module, String operate, Collection<BaseAuth> auths) {
        sysDataAuthComposite.editDataAuths(dataId, module, operate, auths);
    }
}
