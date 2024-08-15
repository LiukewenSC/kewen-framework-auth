package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.model.Result;
import com.kewen.framework.auth.rabc.model.resp.MenuRouteResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    SysAuthMenuComposite sysAuthMenuComposite;

    @GetMapping("/routeMenus")
    public Result<List<MenuRouteResp>> routeTrees(){
        Collection<BaseAuth> auths = AuthUserContext.getAuths();
        List<MenuRouteResp> menuTree = sysAuthMenuComposite.getAuthsMenuRouteTree(auths);
        return Result.success(menuTree);
    }

}