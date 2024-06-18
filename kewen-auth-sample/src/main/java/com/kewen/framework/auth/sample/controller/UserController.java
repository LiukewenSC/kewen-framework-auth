package com.kewen.framework.auth.sample.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.annotation.data.EditDataAuth;
import com.kewen.framework.auth.core.context.CurrentUserAuthContext;
import com.kewen.framework.auth.sample.model.LoginReq;
import com.kewen.framework.auth.sample.model.LoginResp;
import com.kewen.framework.auth.sample.model.Result;
import com.kewen.framework.auth.sample.model.TokenUserStore;
import com.kewen.framework.auth.sys.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.sys.composite.impl.SysUserCompositeImpl;
import com.kewen.framework.auth.sys.model.UserAuthObject;
import com.kewen.framework.auth.sys.model.req.MenuSaveReq;
import com.kewen.framework.auth.sys.model.resp.MenuAuthResp;
import com.kewen.framework.auth.sys.model.resp.MenuResp;
import com.kewen.framework.auth.sys.mp.entity.SysRole;
import com.kewen.framework.auth.sys.mp.entity.SysUser;
import com.kewen.framework.auth.sys.mp.service.SysRoleMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    SysRoleMpService sysRoleMpService;

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
    @GetMapping("/menus")
    public Result<List<MenuResp>> menus(){
        Collection<BaseAuth> auths = CurrentUserAuthContext.getAuths();
        List<MenuResp> menuTree = sysAuthMenuComposite.getCurrentUserMenuTree(auths);
        return Result.success(menuTree);
    }
    @GetMapping("/menuTree")
    public Result<List<MenuAuthResp>> menuTree(){
        List<MenuAuthResp> menuTree = sysAuthMenuComposite.getMenuTree();
        return Result.success(menuTree);
    }
    @PostMapping("/updateMenu")
    public Result<List<MenuResp>> updateMenu(@RequestBody MenuSaveReq req){
        if (req.getAuthType()==2  && (req.getAuthObject()==null || req.getAuthObject().isEmpty())){
            throw new RuntimeException("这样谁都没有权限查看了");
        }
        Collection<BaseAuth> auths = CurrentUserAuthContext.getAuths();
        sysAuthMenuComposite.updateMenu(req);
        return Result.success(null);
    }
    @GetMapping("/roles")
    public Result getRoles(){
        List<SysRole> list = sysRoleMpService.list();
        return Result.success(list);
    }

}
