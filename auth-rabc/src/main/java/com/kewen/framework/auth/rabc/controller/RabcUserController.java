package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.annotation.AuthMenu;
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
    public RabcResult listUser(){
        List<SysUser> list = sysUserMpService.list();
        return RabcResult.success(list);
    }
    @GetMapping("/page")
    @AuthMenu(name = "分页")
    public RabcResult pageUser(@Validated RabcPageReq rabcPageReq){
        RabcPageResult<SysUser> result = RabcPageConverter.pageAndConvert(rabcPageReq,sysUserMpService);
        return RabcResult.success(result);
    }
    @PostMapping("/add")
    @AuthMenu(name = "新增")
    public RabcResult add(@RequestBody SysUser sysUser){
        boolean b = sysUserMpService.save(sysUser);
        return RabcResult.success(b);
    }
    @PostMapping("/update")
    public RabcResult update(@RequestBody SysUser sysUser){
        if (sysUser.getId()==null){
            throw new RuntimeException("用户ID为空");
        }
        boolean b = sysUserMpService.updateById(sysUser);
        return RabcResult.success(b);
    }
    @PostMapping("/delete")
    public RabcResult delete(@RequestBody @Validated RabcIdReq rabcIdReq){
        boolean b = sysUserMpService.removeById(rabcIdReq.getId());
        return RabcResult.success(b);
    }
}
