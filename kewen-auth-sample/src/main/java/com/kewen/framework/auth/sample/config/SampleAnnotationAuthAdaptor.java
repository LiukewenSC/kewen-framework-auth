package com.kewen.framework.auth.sample.config;

import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26 14:36
 */
@Component
public class SampleAnnotationAuthAdaptor implements AnnotationAuthHandler<Long> {

    @Override
    public DataRangeDatabaseField getDataRangeDatabaseField() {
        return new DataRangeDatabaseField();
    }

    @Override
    public boolean hasMenuAccessAuth(IAuthObject authObject, String url) {
        Collection<BaseAuth> baseAuths = authObject.listBaseAuth();
        return false;
    }

    @Override
    public boolean hasDataOperateAuths(IAuthObject authObject, String module, String operate, Long businessId) {
        Collection<BaseAuth> baseAuths = authObject.listBaseAuth();

        return false;
    }

    @Override
    public void editDataAuths(Long businessId, String module, String operate, IAuthObject authObject) {
        Collection<BaseAuth> baseAuths = authObject.listBaseAuth();
    }

}
