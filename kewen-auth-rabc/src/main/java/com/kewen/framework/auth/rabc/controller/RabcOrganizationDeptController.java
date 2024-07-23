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
@RequestMapping("/rabc/dept")
public class RabcOrganizationDeptController {

    @Autowired
    SysDeptMpService sysDeptMpService;


    @GetMapping("/list")
    public Result listDept(){
        List<SysDept> list = sysDeptMpService.list();
        return Result.success(list);
    }
    @GetMapping("/page")
    public Result pageDept(@Validated PageReq req){
        return Result.success(PageConverter.pageAndConvert(req, sysDeptMpService));
    }
    @PostMapping("/add")
    public Result add(@RequestBody SysDept sysDept){
        boolean b = sysDeptMpService.save(sysDept);
        return Result.success(b);
    }
    @PostMapping("/update")
    public Result update(@RequestBody SysDept sysDept){
        if (sysDept.getId()==null){
            throw new RuntimeException("用户ID为空");
        }
        boolean b = sysDeptMpService.updateById(sysDept);
        return Result.success(b);
    }
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated IdReq idReq){
        boolean b = sysDeptMpService.removeById(idReq.getId());
        return Result.success(b);
    }
}
