package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.annotation.AuthMenu;
import com.kewen.framework.auth.rabc.model.*;
import com.kewen.framework.auth.rabc.mp.entity.SysRole;
import com.kewen.framework.auth.rabc.mp.service.SysRoleMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rabc/role")
public class RabcRoleController {


    @Autowired
    SysRoleMpService sysRoleMpService;

    @GetMapping("/list")
    @AuthMenu(name = "角色列表")
    public RabcResult listRole(){
        List<SysRole> list = sysRoleMpService.list();
        return RabcResult.success(list);
    }
    @GetMapping("/page")
    @AuthMenu(name = "角色分页")
    public RabcResult pageRole(@Validated PageReq req){
        PageResult<SysRole> sysRolePageResult = PageConverter.pageAndConvert(req, sysRoleMpService);
        return RabcResult.success(sysRolePageResult);
    }
    @PostMapping("/add")
    @AuthMenu(name = "添加角色")
    public RabcResult add(@RequestBody SysRole sysRole){
        boolean b = sysRoleMpService.save(sysRole);
        return RabcResult.success(b);
    }
    @PostMapping("/update")
    @AuthMenu(name = "修改角色")
    public RabcResult update(@RequestBody SysRole sysUser){
        if (sysUser.getId()==null){
            throw new RuntimeException("用户ID为空");
        }
        boolean b = sysRoleMpService.updateById(sysUser);
        return RabcResult.success(b);
    }
    @PostMapping("/delete")
    @AuthMenu(name = "删除角色")
    public RabcResult delete(@RequestBody @Validated IdReq idReq){
        boolean b = sysRoleMpService.removeById(idReq.getId());
        return RabcResult.success(b);
    }
}
