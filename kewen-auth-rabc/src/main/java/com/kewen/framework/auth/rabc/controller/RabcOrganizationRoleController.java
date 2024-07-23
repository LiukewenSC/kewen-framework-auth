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
@RequestMapping("/rabc/role")
public class RabcOrganizationRoleController {


    @Autowired
    SysRoleMpService sysRoleMpService;

    @GetMapping("/list")
    public Result listRole(){
        List<SysRole> list = sysRoleMpService.list();
        return Result.success(list);
    }
    @GetMapping("/page")
    public Result pageRole(@Validated PageReq req){
        PageResult<SysRole> sysRolePageResult = PageConverter.pageAndConvert(req, sysRoleMpService);
        return Result.success(sysRolePageResult);
    }
    @PostMapping("/add")
    public Result add(@RequestBody SysRole sysRole){
        boolean b = sysRoleMpService.save(sysRole);
        return Result.success(b);
    }
    @PostMapping("/update")
    public Result update(@RequestBody SysRole sysUser){
        if (sysUser.getId()==null){
            throw new RuntimeException("用户ID为空");
        }
        boolean b = sysRoleMpService.updateById(sysUser);
        return Result.success(b);
    }
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated IdReq idReq){
        boolean b = sysRoleMpService.removeById(idReq.getId());
        return Result.success(b);
    }
}
