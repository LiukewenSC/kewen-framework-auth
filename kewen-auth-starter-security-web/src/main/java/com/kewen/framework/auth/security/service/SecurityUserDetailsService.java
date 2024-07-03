package com.kewen.framework.auth.security.service;

import com.kewen.framework.auth.security.model.SecurityUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SecurityUserDetailsService extends UserDetailsService {
    @Override
    SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException;
}
