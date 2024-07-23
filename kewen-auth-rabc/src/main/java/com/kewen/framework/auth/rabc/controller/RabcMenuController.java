package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.model.Result;
import com.kewen.framework.auth.rabc.model.req.MenuSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuAuthResp;
import com.kewen.framework.auth.rabc.model.resp.MenuResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class RabcMenuController {

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;


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
        Collection<BaseAuth> auths = AuthUserContext.getAuths();
        sysAuthMenuComposite.updateMenu(req);
        return Result.success(null);
    }
}
