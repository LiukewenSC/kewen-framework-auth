package com.kewen.framework.auth.core.data;

import com.kewen.framework.auth.core.entity.BaseAuth;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实现了 AuthData配置相关的数据处理
 * @author kewen
 * @since 2024-08-14
 */
@Setter
public class JdbcAuthDataHandler implements AuthDataHandler {

    protected JdbcAuthDataPersistent jdbcAuthDataPersistent;

    @Override
    public void editDataAuths(String businessFunction, Object dataId, String operate, Collection<BaseAuth> baseAuths) {
        //这里为了保证baseAuths不为空，需要为空的时候设置为emptyList，但是直接赋值 lambda中会报错
        Collection<BaseAuth> baseAuthsTemplate = baseAuths==null?Collections.emptyList():baseAuths;

        List<AuthDataDO> dbList=jdbcAuthDataPersistent.listAuthData(businessFunction,dataId,operate);
        //需要添加的
        List<AuthDataDO> adds = baseAuthsTemplate.stream()
                .filter(
                        as -> dbList.stream().noneMatch(db -> db.getAuthority().equals(as.getAuth()))
                ).map(as -> new AuthDataDO(
                        businessFunction,
                        dataId,
                        operate,
                        as.getAuth(),
                        as.getDescription()
                        )
                ).collect(Collectors.toList());
        //需要移除的
        List<Object> removes = dbList.stream()
                .filter(db -> !baseAuthsTemplate.contains(new BaseAuth(db.getAuthority(), db.getDescription())))
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
    public boolean hasDataOperateAuths(Collection<BaseAuth> auths, String businessFunction, Object dataId,String operate) {
        List<AuthDataDO> authDataList = jdbcAuthDataPersistent.listAuthData(businessFunction, dataId, operate);
        //匹配任意一个权限就返回有权限
        return auths.stream().anyMatch(a -> authDataList.stream().anyMatch(ad -> ad.getAuthority().equals(a.getAuth())));
    }

    @Override
    public Collection<BaseAuth> getDataAuths(String businessFunction, Object dataId, String operate) {
        List<AuthDataDO> authDataDOS = jdbcAuthDataPersistent.listAuthData(businessFunction, dataId, operate);
        return authDataDOS.stream().map(
                a -> new BaseAuth(a.getAuthority(), a.getDescription())
        ).collect(Collectors.toList());
    }

    @Override
    public void deleteBusinessFunctionAuthByDataId(String businessFunction, Object dataId) {
        int i = jdbcAuthDataPersistent.deleteBusinessFunctionDataIdAuth(businessFunction, dataId);
    }
}
