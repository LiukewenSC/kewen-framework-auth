package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.model.Result;
import com.kewen.framework.auth.rabc.model.req.MenuApiSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuApiAndAuthResp;
import com.kewen.framework.auth.rabc.model.resp.MenuApiResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/menu/api")
@AuthMenu(name = "菜单路径相关接口")
public class RabcMenuController {

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;


    @GetMapping("/tree")
    public Result<List<MenuApiAndAuthResp>> menuTree(){
        List<MenuApiAndAuthResp> menuTree = sysAuthMenuComposite.getMenuRequestAuthTree();
        return Result.success(menuTree);
    }
    @PostMapping("/update")
    public Result<List<MenuApiResp>> updateMenu(@RequestBody MenuApiSaveReq req){
        if (req.getAuthType()==2  && (req.getAuthObject()==null || req.getAuthObject().isEmpty())){
            throw new RuntimeException("这样谁都没有权限查看了");
        }
        Collection<BaseAuth> auths = AuthUserContext.getAuths();
        sysAuthMenuComposite.updateMenu(req);
        return Result.success(null);
    }
}
