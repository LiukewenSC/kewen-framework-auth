package com.kewen.framework.auth.support;

import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;
import com.kewen.framework.auth.support.impl.DefaultAuthObject;


/**
 * 默认的权限处理器
 * @author kewen
 * @since 2023-12-27
 */
public class DefaultAuthHandler implements ExtensionAuthHandler<DefaultAuthObject,Long> {
    @Override
    public DataRangeDatabaseField getDataRangeDatabaseField() {
        return new DataRangeDatabaseField();
    }

    @Override
    public boolean hasMenuAccessAuth(DefaultAuthObject authObject, String url) {
        return false;
    }

    @Override
    public boolean hasDataOperateAuths(DefaultAuthObject authObject, String module, String operate, Long businessId) {
        return false;
    }

    @Override
    public void editDataAuths(Long businessId, String module, String operate, IAuthObject authObject) {

    }

    @Override
    public boolean hasMenuEditAuth(DefaultAuthObject authObject) {
        return false;
    }

    @Override
    public void editMenuAuths(Long menuId, DefaultAuthObject authObject) {

    }
}
