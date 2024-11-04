package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.AuthMenu;
import com.kewen.framework.auth.core.AuthUserContext;
import com.kewen.framework.auth.core.entity.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.model.MenuTypeConstant;
import com.kewen.framework.auth.rabc.model.RabcResult;
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
public class RabcMenuApiController {

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;


    @GetMapping("/tree")
    public RabcResult<List<MenuApiAndAuthResp>> menuTree(){
        List<MenuApiAndAuthResp> menuTree = sysAuthMenuComposite.getApiAuthMenuTree(true);
        return RabcResult.success(menuTree);
    }
    @PostMapping("/update")
    public RabcResult<List<MenuApiResp>> updateMenu(@RequestBody MenuApiSaveReq req){
        if (req.getAuthType()== MenuTypeConstant.AUTH_TYPE.OWNER && (req.getAuthObject()==null || req.getAuthObject().isEmpty())){
            throw new RuntimeException("这样谁都没有权限查看了");
        }
        Collection<BaseAuth> auths = AuthUserContext.getAuths();
        sysAuthMenuComposite.updateMenu(req);
        return RabcResult.success(null);
    }
}
