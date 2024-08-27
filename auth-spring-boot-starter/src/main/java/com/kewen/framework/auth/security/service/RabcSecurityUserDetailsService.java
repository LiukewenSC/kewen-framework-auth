package com.kewen.framework.auth.security.service;

import com.kewen.framework.auth.rabc.composite.SysUserComposite;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.extension.RabcCurrentUserService;
import com.kewen.framework.auth.rabc.model.UserAuthObject;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.rabc.mp.entity.SysUserCredential;
import com.kewen.framework.auth.security.model.SecurityUser;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 基于Rabc的权限实现体系，前提是要引入了 <kewen-auth-starter-rabc>
 *
 * @author kewen
 * @since 2024-07-02
 */
@Setter
public class RabcSecurityUserDetailsService implements SecurityUserDetailsService, RabcCurrentUserService<SecurityUser> {

    SysUserComposite sysUserComposite;
    PasswordEncoder passwordEncoder;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthObject userAuthObject = sysUserComposite.loadByUsername(username);
        if (userAuthObject == null) {
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
        if (!b) {
            throw new RuntimeException("密码更新失败");
        }
        return user;
    }

    @Override
    public void updatePassword(SecurityUser currentUser, String oldPassword, String newPassword) {
        //原来的上下文中没保存密码，只有再获取一次，也不影响
        SecurityUser securityUser = loadUserByUsername(currentUser.getUsername());
        String dbPassword = securityUser.getPassword();
        if (!passwordEncoder.matches(oldPassword, dbPassword)) {
            throw new RuntimeException("旧密码错误");
        }
        updatePassword(currentUser, passwordEncoder.encode(newPassword));
    }

}
