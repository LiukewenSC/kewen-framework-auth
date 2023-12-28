package com.kewen.framework.auth.sys;

import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;
import com.kewen.framework.auth.sys.composite.SysDataAuthComposite;
import com.kewen.framework.auth.sys.composite.SysMenuAuthComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 默认的注解权限处理器的实现，根据数据库来处理
 * @author kewen
 * @since 2023-12-28
 */
@Component
public class DefaultAnnotationAuthHandler implements AnnotationAuthHandler<Long> {


    @Autowired
    SysMenuAuthComposite sysMenuAuthComposite;

    @Autowired
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
