package com.kewen.framework.auth.security.service;

import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysUserComposite;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.model.UserAuthObject;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.rabc.mp.entity.SysUserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    SysUserComposite sysUserComposite;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthObject userAuthObject = sysUserComposite.loadByUsername(username);
        if (userAuthObject == null){
            throw new UsernameNotFoundException("未找到用户");
        }
        SysUser sysUser = userAuthObject.getSysUser();
        SysUserCredential sysUserCredential = userAuthObject.getSysUserCredential();
        SimpleAuthObject authObject = userAuthObject.getAuthObject();
        List<GrantedAuthority> grantedAuthorities = authObject.listBaseAuth().stream()
                .map(BaseAuth::getAuth)
                .map(a -> (GrantedAuthority) () -> a)
                .collect(Collectors.toList());

        return new SecurityUser(
                sysUser.getUsername(),
                sysUserCredential.getPassword(),
                sysUserCredential.getEnabled(),
                true,
                LocalDateTime.now().isBefore(sysUserCredential.getPasswordExpiredTime()),
                accountNonLocked(sysUserCredential.getAccountLockedDeadline()),
                grantedAuthorities
        );
    }
    private boolean accountNonLocked(LocalDateTime localDateTime) {
        return  localDateTime == null || LocalDateTime.now().isBefore(localDateTime);
    }
}
