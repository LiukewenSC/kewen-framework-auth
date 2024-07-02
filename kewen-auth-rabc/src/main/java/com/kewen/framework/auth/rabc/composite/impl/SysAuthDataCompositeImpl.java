package com.kewen.framework.auth.rabc.composite.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysAuthDataComposite;
import com.kewen.framework.auth.rabc.composite.mapper.SysUserUnionCompositeMapper;
import com.kewen.framework.auth.rabc.mp.entity.SysAuthData;
import com.kewen.framework.auth.rabc.mp.service.SysAuthDataMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author kewen
 * @since 2023-12-28
 */
@Service
public class SysAuthDataCompositeImpl implements SysAuthDataComposite {

    @Autowired
    private SysAuthDataMpService applicationAuthService;

    @Autowired
    SysUserUnionCompositeMapper sysUserUnionCompositeMapper;


    @Override
    public boolean hasDataAuth(Collection<BaseAuth> auths, String module, String operate, Long dataId) {
        Integer integer = sysUserUnionCompositeMapper.hasAuth(auths, module, operate, dataId);
        return integer > 0;
    }

    @Override
    public void editDataAuths(Long dataId, String module, String operate, Collection<BaseAuth> baseAuths) {
        //移除原有的
        applicationAuthService.remove(
                new LambdaQueryWrapper<SysAuthData>().eq(SysAuthData::getData_id,dataId)
        );
        //批量插入新的
        if (!CollectionUtils.isEmpty(baseAuths)){
            applicationAuthService.saveBatch(
                    baseAuths.stream()
                            .map(a->
                                    new SysAuthData()
                                            .setData_id(dataId)
                                            .setModule(module)
                                            .setOperate(operate)
                                            .setAuthority(a.getAuth())
                                            .setDescription(a.getDescription())
                            ).collect(Collectors.toList())
            );
        }
    }
}
