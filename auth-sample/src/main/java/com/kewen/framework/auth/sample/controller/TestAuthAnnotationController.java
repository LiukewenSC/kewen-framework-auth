package com.kewen.framework.auth.sample.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.core.annotation.data.AuthDataOperation;
import com.kewen.framework.auth.core.annotation.data.AuthDataRange;
import com.kewen.framework.auth.core.annotation.data.AuthDataAuthEdit;
import com.kewen.framework.auth.core.annotation.data.authedit.IdDataAuthEdit;
import com.kewen.framework.auth.core.annotation.data.edit.IdDataEdit;
import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import com.kewen.framework.auth.rabc.model.PageReq;
import com.kewen.framework.auth.rabc.model.PageResult;
import com.kewen.framework.auth.rabc.model.Result;
import com.kewen.framework.auth.sample.mp.entity.TestauthAnnotationBusiness;
import com.kewen.framework.auth.sample.mp.service.TestauthAnnotationBusinessMpService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试权限数据范围Controller
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
@RestController
@RequestMapping("/test")
public class TestAuthAnnotationController {

    @Autowired
    TestauthAnnotationBusinessMpService testauthAnnotationBusinessMpService;

    /**
     * 测试数据范围
     * @return
     */
    @AuthDataRange(businessFunction = "testrange")
    @GetMapping("/dataRange")
    public Object testDataRange() {
        List<TestauthAnnotationBusiness> list = testauthAnnotationBusinessMpService.list();
        System.out.println(list);
        Assert.isTrue(list.size()==1, "菜单列表为空");
        return list;
    }


    /**
     * 测试数据范围
     * @return
     */
    @AuthDataRange(businessFunction = "testrange")
    @GetMapping("/pageDataRange")
    public Object pageDataRange(PageReq pageReq) {
        Page<TestauthAnnotationBusiness> page = new Page<>(pageReq.getPage(),pageReq.getSize());
        Page<TestauthAnnotationBusiness> pageResult = testauthAnnotationBusinessMpService.page(page);
        Assert.isTrue(!pageResult.getRecords().isEmpty(), "测试的表数据为空");
        PageResult<TestauthAnnotationBusiness> result = new PageResult<>();
        result.setTotal(pageResult.getTotal());
        result.setData(pageResult.getRecords());
        result.setPage(pageReq.getPage());
        result.setSize(pageResult.getSize());
        return Result.success(result);
    }

    /**
     * 测试数据编辑
     * @return
     */
    @PostMapping("/dataEdit")
    @AuthDataOperation(businessFunction = "testedit")
    public Result testDataEdit(@RequestBody EditIdDataEdit editBusinessData) {
        System.out.println("successEdit");
        return Result.success(editBusinessData);
    }

    /**
     * 测试数据权限编辑
     * @return
     */
    @PostMapping("/dataAuthEdit")
    @AuthDataAuthEdit(businessFunction = "testauthedit")
    public Result<EditIdDataAuthEdit> testDataAuthEdit(@RequestBody EditIdDataAuthEdit applicationBusiness) {
        System.out.println("successdataAuthEdit");

        return Result.success(applicationBusiness);
    }

    /**
     * 测试菜单控制
     * @return
     */
    @AuthMenu(name = "测试菜单控制")
    @GetMapping("/checkMenu")
    public Result testCheckMenu() {
        return Result.success("成功通过注解AuthMenu完成控制");
    }
    /**
     * 测试菜单控制
     * @return
     */
    @AuthMenu(name = "测试菜单控制")
    @GetMapping("/checkMenuNone")
    public String testCheckMenuNone() {
        return "没有权限执行";
    }

    @Data
    public static class EditIdDataEdit implements IdDataEdit<Long> {

        private Long id;

        private String name;

        @Override
        public Long getDataId() {
            return id;
        }
    }
    @Data
    public static class EditIdDataAuthEdit implements IdDataAuthEdit<Long> {

        private Long id;

        @Override
        public Long getDataId() {
            return id;
        }

        @Override
        public IAuthObject getAuthObject() {
            return null;
        }

    }


}