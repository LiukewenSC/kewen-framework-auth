package com.kewen.framework.auth.core.annotation;

import com.kewen.framework.auth.core.annotation.data.AuthDataDO;
import com.kewen.framework.auth.core.annotation.data.JdbcAuthDataPersistent;
import com.kewen.framework.auth.core.model.BaseAuth;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实现了 AuthData配置相关的数据处理
 * @author kewen
 * @since 2024-08-14
 */
@Setter
public abstract class AbstractAuthDatraAnnotationAuthHandler<ID> implements AnnotationAuthHandler<ID>{

    protected JdbcAuthDataPersistent jdbcAuthDataPersistent;

    @Override
    public void editDataAuths(String businessFunction, ID dataId, String operate, Collection<BaseAuth> baseAuths) {
        List<AuthDataDO<ID>> dbList=jdbcAuthDataPersistent.listAuthData(businessFunction,dataId,operate);
        //需要添加的
        List<AuthDataDO<ID>> adds = baseAuths.stream()
                .filter(
                        as -> dbList.stream().noneMatch(db -> db.getAuthority().equals(as.getAuth()))
                ).map(as -> new AuthDataDO<ID>(
                        businessFunction,
                        dataId,
                        operate,
                        as.getAuth(),
                        as.getDescription()
                        )
                ).collect(Collectors.toList());
        //需要移除的
        List<ID> removes = dbList.stream()
                .filter(db -> !baseAuths.contains(new BaseAuth(db.getAuthority(), db.getDescription())))
                .map(AuthDataDO::getId)
                .collect(Collectors.toList());

        //移除原有的
        if (!CollectionUtils.isEmpty(removes)) {
            int i = jdbcAuthDataPersistent.deleteBatchAuthData(removes);
        }
        //批量插入新的
        if (!CollectionUtils.isEmpty(adds)) {
            boolean b = jdbcAuthDataPersistent.insertBatchAuthData(adds);
        }
    }

    @Override
    public boolean hasDataOperateAuths(Collection<BaseAuth> auths, String businessFunction, ID dataId,String operate) {
        List<AuthDataDO<ID>> authDataList = jdbcAuthDataPersistent.listAuthData(businessFunction, dataId, operate);
        //匹配任意一个权限就返回有权限
        return auths.stream().anyMatch(a -> authDataList.stream().anyMatch(ad -> ad.getAuthority().equals(a.getAuth())));
    }

    @Override
    public Collection<BaseAuth> getDataAuths(String businessFunction, ID dataId, String operate) {
        List<AuthDataDO<ID>> authDataDOS = jdbcAuthDataPersistent.listAuthData(businessFunction, dataId, operate);
        return authDataDOS.stream().map(
                a -> new BaseAuth(a.getDescription(), a.getDescription())
        ).collect(Collectors.toList());
    }
}
