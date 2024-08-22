package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.extension.RabcCurrentUserService;
import com.kewen.framework.auth.rabc.model.Result;
import com.kewen.framework.auth.rabc.model.req.UpdatePasswordReq;
import com.kewen.framework.auth.rabc.model.resp.MenuRouteResp;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * @author kewen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/currentUser")
public class RabcCurrentUserController implements ApplicationContextAware {

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;

    RabcCurrentUserService rabcCurrentUserService;

    @GetMapping("/routeMenus")
    public Result<List<MenuRouteResp>> routeTrees(){
        Collection<BaseAuth> auths = AuthUserContext.getAuths();
        List<MenuRouteResp> menuTree = sysAuthMenuComposite.getAuthsMenuRouteTree(auths);
        return Result.success(menuTree);
    }
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody @Validated UpdatePasswordReq req){
        rabcCurrentUserService.updatePassword(req);
        return Result.success();
    }









    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ObjectProvider<RabcCurrentUserService> beanProvider = applicationContext.getBeanProvider(RabcCurrentUserService.class);
        RabcCurrentUserService ifAvailable = beanProvider.getIfAvailable();
        //没有注入RabcCurrentUserService就不支持
        if(ifAvailable != null){
            rabcCurrentUserService = ifAvailable;
        } else {
            rabcCurrentUserService = new UnsupportedRabcCurrentUserService();
        }
    }

    /**
     * 不支持的当前登录服务
     */
    static class UnsupportedRabcCurrentUserService implements RabcCurrentUserService {

        @Override
        public void updatePassword(UpdatePasswordReq req) {
            throw new UnsupportedOperationException("Not supported updatePassword");
        }
    }
}