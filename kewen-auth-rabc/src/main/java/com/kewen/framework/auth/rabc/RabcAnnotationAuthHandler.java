package com.kewen.framework.auth.rabc;

import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;
import com.kewen.framework.auth.rabc.composite.SysAuthDataComposite;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import lombok.Setter;

import java.util.Collection;

/**
 * 默认的注解权限处理器的实现，根据数据库来处理
 *
 * @author kewen
 * @since 2023-12-28
 */
public class RabcAnnotationAuthHandler implements AnnotationAuthHandler<Long> {

    SysAuthMenuComposite sysAuthMenuComposite;

    SysAuthDataComposite sysAuthDataComposite;

    DataRangeDatabaseField dataRangeDatabaseField;



    @Override
    public boolean hasMenuAccessAuth(Collection<BaseAuth> auths, String path) {
        return sysAuthMenuComposite.hasMenuAuth(auths, path);
    }




    @Override
    public DataRangeDatabaseField getDataRangeDatabaseField() {
        return dataRangeDatabaseField;
    }

    @Override
    public boolean hasDataOperateAuths(Collection<BaseAuth> auths, String module, String operate, Long dataId) {
        return sysAuthDataComposite.hasDataAuth(auths, module, operate, dataId);
    }

    @Override
    public void editDataAuths(Long dataId, String module, String operate, Collection<BaseAuth> auths) {
        sysAuthDataComposite.editDataAuths(dataId, module, operate, auths);
    }

    public void setSysAuthMenuComposite(SysAuthMenuComposite sysAuthMenuComposite) {
        this.sysAuthMenuComposite = sysAuthMenuComposite;
    }

    public void setSysAuthDataComposite(SysAuthDataComposite sysAuthDataComposite) {
        this.sysAuthDataComposite = sysAuthDataComposite;
    }

    public void setDataRangeDatabaseField(DataRangeDatabaseField dataRangeDatabaseField) {
        this.dataRangeDatabaseField = dataRangeDatabaseField;
    }
}
