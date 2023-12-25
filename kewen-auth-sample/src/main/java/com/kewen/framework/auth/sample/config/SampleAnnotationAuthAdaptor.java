package com.kewen.framework.auth.sample.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthAdaptor;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26 14:36
 */
@Component
public class SampleAnnotationAuthAdaptor implements AnnotationAuthAdaptor {

    @Override
    public DataRangeDatabaseField getDataRangeDatabaseField() {
        return new DataRangeDatabaseField();
    }

    @Override
    public boolean hasMenuAccessAuth(Collection authorities, String url) {
        System.out.println("hasMenuAccessAuth");
        return false;
    }

    @Override
    public boolean hasDataOperateAuths(Collection auths, String module, String operate, Object businessId) {
        System.out.println("hasDataOperateAuths");
        return false;
    }

    @Override
    public void editDataAuths(Object businessId, String module, String operate, List auths) {
        System.out.println("editDataAuths success");
    }
}
