package com.kewen.framework.auth.sys.composite.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kewen.framework.auth.support.SimpleAuthObject;
import com.kewen.framework.auth.sys.composite.SysUserComposite;
import com.kewen.framework.auth.sys.composite.mapper.SysUserUnionCompositeMapper;
import com.kewen.framework.auth.sys.model.UserAuthObject;
import com.kewen.framework.auth.sys.mp.entity.SysUser;
import com.kewen.framework.auth.sys.mp.entity.SysUserCredential;
import com.kewen.framework.auth.sys.mp.service.SysUserCredentialMpService;
import com.kewen.framework.auth.sys.mp.service.SysUserMpService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kewen
 * @since 2024-05-11
 */
public class SysUserCompositeImpl implements SysUserComposite {

    @Autowired
    SysUserMpService userMpService;

    @Autowired
    SysUserCredentialMpService credentialMpService;

    @Autowired
    SysUserUnionCompositeMapper unionCompositeMapper;

    @Override
    public UserAuthObject login(String username, String password) {
        SysUser user = userMpService.getOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
                        .select()
        );
        if (user == null){
            return null;
        }
        SysUserCredential credential = credentialMpService.getOne(

                new LambdaQueryWrapper<SysUserCredential>().eq(SysUserCredential::getUserId, user.getId())
                        .select()
        );
        if (credential == null){
            return null;
        }
        if (!credential.getPassword().equals(password)){
            return null;
        }
        //查找用户权限体
        SimpleAuthObject authObject = unionCompositeMapper.getUserAuthObject(user.getId());

        UserAuthObject userAuthObject = new UserAuthObject();

        userAuthObject.setAuthObject(authObject);
        userAuthObject.setSysUser(user);
        return userAuthObject;
    }
}
