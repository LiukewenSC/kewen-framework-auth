package com.kewen.framework.auth.security.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.core.context.CurrentUser;
import com.kewen.framework.auth.rabc.extension.RabcCurrentUserService;
import com.kewen.framework.auth.rabc.model.req.UpdatePasswordReq;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.rabc.mp.entity.SysUserCredential;
import com.kewen.framework.auth.rabc.mp.service.SysUserCredentialMpService;
import com.kewen.framework.auth.rabc.mp.service.SysUserMpService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author kewen
 * @since 2024-08-22
 */
public class SecurityRabcCurrentUserService implements RabcCurrentUserService {

    private SysUserCredentialMpService sysUserCredentialMpService;

    private SysUserMpService sysUserMpService;

    private PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(UpdatePasswordReq req) {
        CurrentUser currentUser = AuthUserContext.getCurrentUser();
        Long userId = sysUserMpService.getOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, currentUser.getUsername())
                        .select(SysUser::getId)
        ).getId();
        SysUserCredential dbCredential = sysUserCredentialMpService.getOne(
                new LambdaQueryWrapper<SysUserCredential>()
                        .eq(SysUserCredential::getUserId, userId)
        );
        boolean matches = passwordEncoder.matches(req.getOldPassword(), dbCredential.getPassword());
        if (!matches) {
            throw new RuntimeException("旧密码不正确");
        } else {
            sysUserCredentialMpService.updateById(
                    new SysUserCredential().setId(dbCredential.getId())
                            .setPassword(passwordEncoder.encode(req.getNewPassword()))
            );
        }
    }

    public void setSysUserCredentialMpService(SysUserCredentialMpService sysUserCredentialMpService) {
        this.sysUserCredentialMpService = sysUserCredentialMpService;
    }

    public void setSysUserMpService(SysUserMpService sysUserMpService) {
        this.sysUserMpService = sysUserMpService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
