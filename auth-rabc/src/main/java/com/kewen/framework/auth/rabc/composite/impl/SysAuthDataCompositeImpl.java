package com.kewen.framework.auth.rabc.composite.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysAuthDataComposite;
import com.kewen.framework.auth.rabc.composite.mapper.SysUserUnionCompositeMapper;
import com.kewen.framework.auth.rabc.mp.entity.SysAuthData;
import com.kewen.framework.auth.rabc.mp.service.SysAuthDataMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
    public boolean hasDataAuth(Collection<BaseAuth> auths, String businessFunction, String operate, Long dataId) {
        Integer integer = sysUserUnionCompositeMapper.hasAuth(auths, businessFunction, operate, dataId);
        return integer > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void editDataAuths(String businessFunction, Long dataId, String operate, Collection<BaseAuth> baseAuths) {
        List<SysAuthData> dbList = applicationAuthService.list(
                new LambdaQueryWrapper<SysAuthData>()
                        .eq(SysAuthData::getBusinessFunction, businessFunction)
                        .eq(SysAuthData::getDataId, dataId)
                        .eq(SysAuthData::getOperate, operate)
        );

        //需要添加的
        Collection<SysAuthData> adds = baseAuths.stream()
                .filter(
                        as -> dbList.stream().noneMatch(db -> db.getAuthority().equals(as.getAuth()))
                ).map(as -> new SysAuthData()
                        .setBusinessFunction(businessFunction)
                        .setDataId(dataId)
                        .setOperate(operate)
                        .setAuthority(as.getAuth())
                        .setDescription(as.getDescription())
                ).collect(Collectors.toList());
        //需要移除的
        List<SysAuthData> removes = dbList.stream()
                .filter(db -> !baseAuths.contains(new BaseAuth(db.getAuthority(), db.getDescription())))
                .collect(Collectors.toList());

        //移除原有的
        if (!CollectionUtils.isEmpty(removes)) {
            boolean b = applicationAuthService.removeBatchByIds(removes);
        }
        //批量插入新的
        if (!CollectionUtils.isEmpty(adds)) {
            applicationAuthService.saveBatch(adds);
        }
    }

    @Override
    public Collection<BaseAuth> getDataAuths(String businessFunction, Long dataId, String operate) {
        List<SysAuthData> datas = applicationAuthService.list(
                new LambdaQueryWrapper<SysAuthData>()
                        .eq(SysAuthData::getBusinessFunction, businessFunction)
                        .eq(SysAuthData::getDataId, dataId)
                        .eq(SysAuthData::getOperate, operate)
        );
        return datas.stream().map(d -> new BaseAuth(d.getAuthority(), d.getDescription())).collect(Collectors.toList());
    }
}
