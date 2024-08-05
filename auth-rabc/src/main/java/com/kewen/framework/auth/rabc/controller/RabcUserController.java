package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import com.kewen.framework.auth.rabc.model.*;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.rabc.mp.service.SysUserMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 用户
 *
 * @author kewen
 * @since 2024-07-29
 */
@RestController
@RequestMapping("/rabc/user")
@AuthMenu(name = "用户相关")
public class RabcUserController {

    @Autowired
    SysUserMpService sysUserMpService;

    @GetMapping("/list")
    @AuthMenu(name = "用户列表")
    public Result listUser(){
        List<SysUser> list = sysUserMpService.list();
        return Result.success(list);
    }
    @GetMapping("/page")
    @AuthMenu(name = "分页")
    public Result pageUser(@Validated PageReq pageReq){
        PageResult<SysUser> result = PageConverter.pageAndConvert(pageReq,sysUserMpService);
        return Result.success(result);
    }
    @PostMapping("/add")
    @AuthMenu(name = "新增")
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
    public Result delete(@RequestBody @Validated IdReq idReq){
        boolean b = sysUserMpService.removeById(idReq.getId());
        return Result.success(b);
    }
}
