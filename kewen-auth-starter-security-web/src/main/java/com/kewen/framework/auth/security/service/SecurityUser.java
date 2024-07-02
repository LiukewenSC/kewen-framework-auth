package com.kewen.framework.auth.security.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class SecurityUser<T extends IAuthObject> implements UserDetails {


    private String token;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private Long avatarFileId;

    /**
     * 密码
     */
    private String password;

    /**
     * 1-男 2-女 3-..
     */
    private Integer gender;

    /**
     * 权限集合
     */
    private T authObject;

    /**
     * 凭证过期时间 每次修改密码应修改过期时间 ， 为空表示系统无过期时间设定
     */
    private LocalDateTime passwordExpiredTime;

    /**
     * 账号锁定截止时间，为空或早于当前时间则为不锁定
     */
    private LocalDateTime accountLockedDeadline;

    /**
     * 账号是否启用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


    private boolean isNonExpired(LocalDateTime localDateTime) {
        return localDateTime == null || LocalDateTime.now().isBefore(localDateTime);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNonExpired(this.accountLockedDeadline);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isNonExpired(this.passwordExpiredTime);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authObject.listBaseAuth().stream()
                .map(BaseAuth::getAuth)
                .map(a -> (GrantedAuthority) () -> a)
                .collect(Collectors.toList());
    }
}
