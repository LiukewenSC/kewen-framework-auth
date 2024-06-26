package com.kewen.framework.auth.sample.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kewen.framework.auth.core.exception.AuthorizationException;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.sample.exception.AuthenticationException;
import com.kewen.framework.auth.sample.model.*;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.composite.impl.SysUserCompositeImpl;
import com.kewen.framework.auth.rabc.model.UserAuthObject;
import com.kewen.framework.auth.rabc.model.req.MenuSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuAuthResp;
import com.kewen.framework.auth.rabc.model.resp.MenuResp;
import com.kewen.framework.auth.rabc.mp.entity.SysDept;
import com.kewen.framework.auth.rabc.mp.entity.SysRole;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.rabc.mp.service.SysDeptMpService;
import com.kewen.framework.auth.rabc.mp.service.SysRoleMpService;
import com.kewen.framework.auth.rabc.mp.service.SysUserMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    
    @Autowired
    SysDeptMpService sysDeptMpService;

    @Autowired
    SysUserMpService sysUserMpService;

    @PostMapping("/login")
    public Result<LoginResp> login(@Valid @RequestBody LoginReq req){
        UserAuthObject userAuthObject = sysUserComposite.loadByUsername(req.getUsername());
        boolean isLogin = Optional.ofNullable(userAuthObject)
                .map(u -> u.getSysUserCredential())
                .map(c -> c.getPassword())
                .filter(p -> req.getPassword().equals(p))
                .isPresent();
        if (!isLogin){
            throw new AuthenticationException("登录失败，用户名或密码错误");
        }
        LoginResp loginResp = new LoginResp();
        BeanUtil.copyProperties(userAuthObject,loginResp);
        String token = UUID.fastUUID().toString().replaceAll("-","");
        TokenUserStore.set(token,userAuthObject);
        loginResp.setToken(token);
        return Result.success(loginResp);
    }
    @GetMapping("/menus")
    public Result<List<MenuResp>> menus(){
        Collection<BaseAuth> auths = AuthUserContext.getAuths();
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
        Collection<BaseAuth> auths = AuthUserContext.getAuths();
        sysAuthMenuComposite.updateMenu(req);
        return Result.success(null);
    }
    @GetMapping("/roles")
    public Result getRoles(){
        List<SysRole> list = sysRoleMpService.list();
        return Result.success(list);
    }
    @GetMapping("/depts")
    public Result depts(){
        List<SysDept> list = sysDeptMpService.list();
        return Result.success(list);
    }
    @GetMapping("/pageUser")
    public Result pageUser(@Validated PageReq pageReq){
        Page<SysUser> userPage = new Page<>(pageReq.getPage() ,pageReq.getSize());
        Page<SysUser> page = sysUserMpService.page(userPage);
        List<SysUser> records = page.getRecords();
        PageResult<SysUser> result = new PageResult<>();
        result.setPage(pageReq.getPage());
        result.setSize(pageReq.getSize());
        result.setTotal((int)page.getTotal());
        result.setData(records);
        return Result.success(result);
    }
    @PostMapping("/add")
    public Result add(@RequestBody SysUser sysUser){
        boolean b = sysUserMpService.save(sysUser);
        return Result.success(b);
    }
    @PostMapping("/update")
    public Result update(@RequestBody SysUser sysUser){
        if (sysUser.getId()==null){
            throw new RuntimeException("用户ID为空");
        }
        boolean b = sysUserMpService.updateById(sysUser);
        return Result.success(b);
    }
    @PostMapping("/delete")
    public Result delete(@RequestBody IdReq idReq){
        if (idReq.getId()==null){
            throw new RuntimeException("用户ID为空");
        }
        boolean b = sysUserMpService.removeById(idReq.getId());
        return Result.success(b);
    }
}