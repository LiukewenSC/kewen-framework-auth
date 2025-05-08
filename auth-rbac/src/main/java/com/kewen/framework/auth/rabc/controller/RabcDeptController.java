package com.kewen.framework.auth.rabc.controller;

import com.kewen.framework.auth.core.AuthMenu;
import com.kewen.framework.auth.rabc.model.*;
import com.kewen.framework.auth.rabc.mp.entity.SysDept;
import com.kewen.framework.auth.rabc.mp.service.SysDeptMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rabc/dept")
public class RabcDeptController {

    @Autowired
    SysDeptMpService sysDeptMpService;


    @GetMapping("/list")
    @AuthMenu(name = "部门列表")
    public RabcResult listDept(){
        List<SysDept> list = sysDeptMpService.list();
        return RabcResult.success(list);
    }
    @GetMapping("/page")
    @AuthMenu(name = "部门分页")
    public RabcResult pageDept(@Validated RabcPageReq req){
        return RabcResult.success(RabcPageConverter.pageAndConvert(req, sysDeptMpService));
    }
    @PostMapping("/add")
    @AuthMenu(name = "添加部门")
    public RabcResult add(@RequestBody SysDept sysDept){
        boolean b = sysDeptMpService.save(sysDept);
        return RabcResult.success(b);
    }
    @PostMapping("/update")
    @AuthMenu(name = "部门列表")
    public RabcResult update(@RequestBody SysDept sysDept){
        if (sysDept.getId()==null){
            throw new RuntimeException("用户ID为空");
        }
        boolean b = sysDeptMpService.updateById(sysDept);
        return RabcResult.success(b);
    }
    @PostMapping("/delete")
    @AuthMenu(name = "删除部门")
    public RabcResult delete(@RequestBody @Validated RabcIdReq rabcIdReq){
        boolean b = sysDeptMpService.removeById(rabcIdReq.getId());
        return RabcResult.success(b);
    }
}
