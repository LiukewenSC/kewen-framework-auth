package com.kewen.framework.auth.sample.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.context.CurrentUserAuthContext;
import com.kewen.framework.auth.sample.model.LoginReq;
import com.kewen.framework.auth.sample.model.LoginResp;
import com.kewen.framework.auth.sample.model.Result;
import com.kewen.framework.auth.sample.model.TokenUserStore;
import com.kewen.framework.auth.sys.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.sys.composite.impl.SysUserCompositeImpl;
import com.kewen.framework.auth.sys.model.UserAuthObject;
import com.kewen.framework.auth.sys.model.resp.MenuResp;
import com.kewen.framework.auth.sys.mp.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * @author kewen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    SysUserCompositeImpl sysUserComposite;

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;

    @PostMapping("/login")
    public Result<LoginResp> login(@Valid @RequestBody LoginReq req){
        UserAuthObject userAuthObject = sysUserComposite.login(req.getUsername(), req.getPassword());
        LoginResp loginResp = new LoginResp();
        BeanUtil.copyProperties(userAuthObject,loginResp);
        String token = UUID.fastUUID().toString().replaceAll("-","");
        TokenUserStore.set(token,userAuthObject);
        loginResp.setToken(token);
        return Result.success(loginResp);
    }
    @PostMapping("/menus")
    public Result<List<MenuResp>> menus(){
        Collection<BaseAuth> auths = CurrentUserAuthContext.getAuths();
        List<MenuResp> menuTree = sysAuthMenuComposite.getCurrentUserMenuTree(auths);
        return Result.success(menuTree);
    }

}
