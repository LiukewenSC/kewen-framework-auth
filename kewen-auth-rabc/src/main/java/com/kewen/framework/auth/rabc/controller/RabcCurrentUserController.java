package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.composite.impl.SysUserCompositeImpl;
import com.kewen.framework.auth.rabc.model.*;
import com.kewen.framework.auth.rabc.model.resp.MenuResp;
import com.kewen.framework.auth.rabc.mp.service.SysDeptMpService;
import com.kewen.framework.auth.rabc.mp.service.SysRoleMpService;
import com.kewen.framework.auth.rabc.mp.service.SysUserMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * @author kewen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/currentUser")
public class RabcCurrentUserController {

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


    @GetMapping("/menus")
    public Result<List<MenuResp>> menus(){
        Collection<BaseAuth> auths = AuthUserContext.getAuths();
        List<MenuResp> menuTree = sysAuthMenuComposite.getCurrentUserMenuTree(auths);
        return Result.success(menuTree);
    }

}