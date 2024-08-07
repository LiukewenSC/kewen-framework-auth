package com.kewen.framework.auth.security.service;

import com.kewen.framework.auth.rabc.composite.SysUserComposite;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.model.UserAuthObject;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.rabc.mp.entity.SysUserCredential;
import com.kewen.framework.auth.security.model.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * 基于Rabc的权限实现体系，前提是要引入了 <kewen-auth-starter-rabc>
 * @author kewen
 * @since 2024-07-02
 */
public class RabcSecurityUserDetailsService implements SecurityUserDetailsService {

    SysUserComposite sysUserComposite;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthObject userAuthObject = sysUserComposite.loadByUsername(username);
        if (userAuthObject == null){
            throw new UsernameNotFoundException("未找到用户");
        }
        SysUser sysUser = userAuthObject.getSysUser();
        SysUserCredential credential = userAuthObject.getSysUserCredential();
        SimpleAuthObject authObject = userAuthObject.getAuthObject();

         return new SecurityUser()
                 .setId(sysUser.getId())
                 .setName(sysUser.getName())
                 .setUsername(sysUser.getUsername())
                 .setNickName(sysUser.getNickName())
                 .setPhone(sysUser.getPhone())
                 .setEmail(sysUser.getEmail())
                 .setAvatarFileId(sysUser.getAvatarFileId())
                 .setPassword(credential.getPassword())
                 .setGender(sysUser.getGender())
                 .setAuthObject(authObject)
                 .setPasswordExpiredTime(credential.getPasswordExpiredTime())
                 .setAccountLockedDeadline(credential.getAccountLockedDeadline())
                 .setEnabled(credential.getEnabled())
                 .setCreateTime(sysUser.getCreateTime())
                 .setUpdateTime(sysUser.getUpdateTime())
         ;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        boolean b = sysUserComposite.updatePassword(user.getUsername(), newPassword);
        if (!b){
            throw new RuntimeException("密码更新失败");
        }
        return user;
    }

    public RabcSecurityUserDetailsService setSysUserComposite(SysUserComposite sysUserComposite) {
        this.sysUserComposite = sysUserComposite;
        return this;
    }
}
