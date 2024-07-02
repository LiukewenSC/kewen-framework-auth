package com.kewen.framework.auth.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.composite.impl.SysUserCompositeImpl;
import com.kewen.framework.auth.rabc.model.*;
import com.kewen.framework.auth.rabc.mp.service.SysDeptMpService;
import com.kewen.framework.auth.rabc.mp.service.SysRoleMpService;
import com.kewen.framework.auth.rabc.mp.service.SysUserMpService;
import com.kewen.framework.auth.web.TokenUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SysUserCompositeImpl sysUserComposite;

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;

    @Autowired
    SysRoleMpService sysRoleMpService;

    @Autowired
    SysDeptMpService sysDeptMpService;

    @Autowired
    SysUserMpService sysUserMpService;

    @PostMapping("${kewen-framework.security.login.login-url}")
    //@PostMapping("/login")
    public Result<LoginResp> login(@Valid @RequestBody LoginReq req){
        UserAuthObject userAuthObject = sysUserComposite.loadByUsername(req.getUsername());
        boolean isLogin = Optional.ofNullable(userAuthObject)
                .map(u -> u.getSysUserCredential())
                .map(c -> c.getPassword())
                .filter(p -> passwordEncoder.matches(req.getPassword(),p))
                .isPresent();
        if (!isLogin){
            throw new RuntimeException("登录失败，用户名或密码错误");
        }
        LoginResp loginResp = new LoginResp();
        BeanUtil.copyProperties(userAuthObject,loginResp);
        String token = UUID.fastUUID().toString().replaceAll("-","");
        TokenUserStore.set(token,userAuthObject);
        loginResp.setToken(token);
        return Result.success(loginResp);
    }
}
