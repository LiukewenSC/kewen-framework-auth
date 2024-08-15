package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.model.Result;
import com.kewen.framework.auth.rabc.model.req.MenuApiSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuApiAndAuthResp;
import com.kewen.framework.auth.rabc.model.resp.MenuApiResp;
import com.kewen.framework.auth.rabc.model.resp.MenuRouteResp;
import com.kewen.framework.auth.rabc.mp.service.SysMenuRouteMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/menu/route")
@AuthMenu(name = "菜单路由相关接口")
public class RabcMenuRouteController {

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;

    /**
     * 获取所有菜单路由
     * @return
     */
    @GetMapping("/tree")
    public Result<List<MenuRouteResp>> routeTrees(){
        List<MenuRouteResp> menuTree = sysAuthMenuComposite.getAuthsMenuRouteTree();
        return Result.success(menuTree);
    }

}