package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.rabc.model.*;
import com.kewen.framework.auth.rabc.mp.entity.SysDept;
import com.kewen.framework.auth.rabc.mp.entity.SysRole;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.rabc.mp.service.SysDeptMpService;
import com.kewen.framework.auth.rabc.mp.service.SysRoleMpService;
import com.kewen.framework.auth.rabc.mp.service.SysUserMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class RabcOrganizationUserController {

    @Autowired
    SysUserMpService sysUserMpService;

    @GetMapping("/list")
    public Result listUser(){
        List<SysUser> list = sysUserMpService.list();
        return Result.success(list);
    }
    @GetMapping("/pageUser")
    public Result pageUser(@Validated PageReq pageReq){
        PageResult<SysUser> result = PageConverter.pageAndConvert(pageReq,sysUserMpService);
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
    public Result delete(@RequestBody @Validated IdReq idReq){
        boolean b = sysUserMpService.removeById(idReq.getId());
        return Result.success(b);
    }
}
