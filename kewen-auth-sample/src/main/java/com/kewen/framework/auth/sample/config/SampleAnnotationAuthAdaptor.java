package com.kewen.framework.auth.sample.config;

import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.annotation.AnnotationAuthAdaptor;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;
import org.springframework.stereotype.Component;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26 14:36
 */
@Component
public class SampleAnnotationAuthAdaptor implements AnnotationAuthAdaptor<Long> {

    @Override
    public DataRangeDatabaseField getDataRangeDatabaseField() {
        return new DataRangeDatabaseField();
    }

    @Override
    public boolean hasMenuAccessAuth(IAuthObject authObject, String url) {
        return false;
    }

    @Override
    public boolean hasDataOperateAuths(IAuthObject authObject, String module, String operate, Long businessId) {
        return false;
    }

    @Override
    public void editDataAuths(Long businessId, String module, String operate, IAuthObject authObject) {

    }

}
