package com.kewen.framework.auth.sys;

import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;
import com.kewen.framework.auth.sys.composite.SysAuthDataComposite;
import com.kewen.framework.auth.sys.composite.SysAuthMenuComposite;

import java.util.Collection;

/**
 * 默认的注解权限处理器的实现，根据数据库来处理
 *
 * @author kewen
 * @since 2023-12-28
 */
public class AnnotationAuthHandlerImpl implements AnnotationAuthHandler<Long> {


    SysAuthMenuComposite sysAuthMenuComposite;

    SysAuthDataComposite sysAuthDataComposite;

    @Override
    public DataRangeDatabaseField getDataRangeDatabaseField() {
        DataRangeDatabaseField field = new DataRangeDatabaseField();
        field.setTableName("sys_auth_data");
        field.setDataIdColumn("data_id");
        field.setAuthorityColumn("authority");
        return field;
    }

    @Override
    public boolean hasMenuAccessAuth(Collection<BaseAuth> auths, String url) {
        return sysAuthMenuComposite.hasMenuAuth(auths, url);
    }

    @Override
    public boolean hasDataOperateAuths(Collection<BaseAuth> auths, String module, String operate, Long dataId) {
        return sysAuthDataComposite.hasDataAuth(auths, module, operate, dataId);
    }

    @Override
    public void editDataAuths(Long dataId, String module, String operate, Collection<BaseAuth> auths) {
        sysAuthDataComposite.editDataAuths(dataId, module, operate, auths);
    }
}
