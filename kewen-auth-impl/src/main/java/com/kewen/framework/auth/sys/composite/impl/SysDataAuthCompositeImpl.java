package com.kewen.framework.auth.sys.composite.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.sys.composite.SysDataAuthComposite;
import com.kewen.framework.auth.sys.composite.mapper.SysUserCompositeMapper;
import com.kewen.framework.auth.sys.mp.entity.SysApplicationAuth;
import com.kewen.framework.auth.sys.mp.service.SysApplicationAuthMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kewen
 * @since 2023-12-28
 */
@Component
public class SysDataAuthCompositeImpl implements SysDataAuthComposite {

    @Autowired
    private SysApplicationAuthMpService applicationAuthService;

    @Autowired
    SysUserCompositeMapper sysUserCompositeMapper;


    @Override
    public boolean hasDataAuth(Collection<BaseAuth> auths, String module, String operate, Long dataId) {
        Integer integer = sysUserCompositeMapper.hasAuth(auths, module, operate, dataId);
        return integer > 0;
    }

    @Override
    public void editDataAuths(Long dataId, String module, String operate, Collection<BaseAuth> baseAuths) {
        //移除原有的
        applicationAuthService.remove(
                new LambdaQueryWrapper<SysApplicationAuth>().eq(SysApplicationAuth::getBusinessId,dataId)
        );
        //批量插入新的
        if (!CollectionUtils.isEmpty(baseAuths)){
            applicationAuthService.saveBatch(
                    baseAuths.stream()
                            .map(a->
                                    new SysApplicationAuth()
                                            .setBusinessId(dataId)
                                            .setModule(module)
                                            .setOperate(operate)
                                            .setAuthority(a.getAuth())
                                            .setDescription(a.getDescription())
                            ).collect(Collectors.toList())
            );
        }
    }
}
