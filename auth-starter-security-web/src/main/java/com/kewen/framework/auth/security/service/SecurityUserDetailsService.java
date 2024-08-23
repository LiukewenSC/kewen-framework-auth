package com.kewen.framework.auth.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SecurityUserDetailsService extends UserDetailsService, UserDetailsPasswordService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    @Override
    UserDetails updatePassword(UserDetails user, String newPassword);
}
