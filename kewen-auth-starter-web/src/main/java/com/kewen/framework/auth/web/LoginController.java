package com.kewen.framework.auth.web;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.composite.impl.SysUserCompositeImpl;
import com.kewen.framework.auth.rabc.model.*;
import com.kewen.framework.auth.rabc.mp.service.SysDeptMpService;
import com.kewen.framework.auth.rabc.mp.service.SysRoleMpService;
import com.kewen.framework.auth.rabc.mp.service.SysUserMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Optional;

public class LoginController {

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



    @PostMapping("/login")
    public Result<LoginResp> login(@Valid @RequestBody LoginReq req){
        UserAuthObject userAuthObject = sysUserComposite.loadByUsername(req.getUsername());
        boolean isLogin = Optional.ofNullable(userAuthObject)
                .map(u -> u.getSysUserCredential())
                .map(c -> c.getPassword())
                .filter(p -> req.getPassword().equals(p))
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
