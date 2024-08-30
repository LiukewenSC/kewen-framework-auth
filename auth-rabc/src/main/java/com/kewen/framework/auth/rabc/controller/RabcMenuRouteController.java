package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.annotation.AuthMenu;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.model.RabcResult;
import com.kewen.framework.auth.rabc.model.resp.MenuRouteResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public RabcResult<List<MenuRouteResp>> routeTrees(){
        List<MenuRouteResp> menuTree = sysAuthMenuComposite.getRouteAuthMenuTree(true);
        return RabcResult.success(menuTree);
    }

}
