package com.kewen.framework.auth.rabc;

import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.range.AuthDataTable;
import com.kewen.framework.auth.rabc.composite.SysAuthDataComposite;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;

import java.util.Collection;
import java.util.Collections;

/**
 * 默认的注解权限处理器的实现，根据数据库来处理
 *
 * @author kewen
 * @since 2023-12-28
 */
public class RabcAnnotationAuthHandler implements AnnotationAuthHandler<Long> {

    SysAuthMenuComposite sysAuthMenuComposite;

    SysAuthDataComposite sysAuthDataComposite;



    @Override
    public boolean hasMenuAccessAuth(Collection<BaseAuth> auths, String path) {
        return sysAuthMenuComposite.hasMenuAuth(auths, path);
    }

    @Override
    public boolean hasDataOperateAuths(Collection<BaseAuth> auths, String businessFunction, String operate, Long dataId) {
        return sysAuthDataComposite.hasDataAuth(auths, businessFunction, operate, dataId);
    }

    @Override
    public void editDataAuths(String businessFunction, Long dataId, String operate, Collection<BaseAuth> auths) {
        sysAuthDataComposite.editDataAuths(businessFunction,dataId,  operate, auths);
    }

    @Override
    public Collection<BaseAuth> getDataAuths(String businessFunction,Long dataId,  String operate) {
        return sysAuthDataComposite.getDataAuths(businessFunction,dataId,operate);
    }

    public void setSysAuthMenuComposite(SysAuthMenuComposite sysAuthMenuComposite) {
        this.sysAuthMenuComposite = sysAuthMenuComposite;
    }

    public void setSysAuthDataComposite(SysAuthDataComposite sysAuthDataComposite) {
        this.sysAuthDataComposite = sysAuthDataComposite;
    }
}
