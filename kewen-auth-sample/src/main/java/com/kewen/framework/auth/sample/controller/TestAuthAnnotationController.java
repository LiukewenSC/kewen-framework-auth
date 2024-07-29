package com.kewen.framework.auth.sample.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.core.annotation.data.AuthCheckDataOperation;
import com.kewen.framework.auth.core.annotation.data.AuthDataRange;
import com.kewen.framework.auth.core.annotation.data.AuthEditDataAuth;
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
    @AuthDataRange(module = "testrange")
    @GetMapping("/dataRange")
    public Object testDataRange() {
        //直接测试菜单的权限就知道了，
        List<TestauthAnnotationBusiness> list = testauthAnnotationBusinessMpService.list();
        System.out.println(list);
        Assert.isTrue(list.size()==1, "菜单列表为空");
        return list;
    }


    /**
     * 测试数据范围
     * @return
     */
    @AuthDataRange(module = "testrange")
    @GetMapping("/pageDataRange")
    public Object pageDataRange(PageReq pageReq) {
        //直接测试菜单的权限就知道了，
        Page<TestauthAnnotationBusiness> page = new Page<>(pageReq.getPage(),pageReq.getSize());
        Page<TestauthAnnotationBusiness> pageResult = testauthAnnotationBusinessMpService.page(page);
        Assert.isTrue(!pageResult.getRecords().isEmpty(), "菜单列表为空");
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
    @AuthCheckDataOperation(module = "testedit")
    public String testDataEdit(@RequestBody EditIdDataEdit editBusinessData) {
        System.out.println("successEdit");
        return "测试编辑通过，可以编辑数据";
    }

    /**
     * 测试数据权限编辑
     * @return
     */
    @PostMapping("/dataAuthEdit")
    @AuthEditDataAuth(module = "testauthedit")
    public String testDataAuthEdit(@RequestBody EditIdDataAuthEdit applicationBusiness) {

        return "testDataAuthEdit";
    }

    /**
     * 测试菜单控制
     * @return
     */
    @AuthMenu
    @GetMapping("/checkMenu")
    public String testCheckMenu() {

        return "testCheckMenu";
    }

    @Data
    public static class EditIdDataEdit implements IdDataEdit<Long> {

        private Long id;

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
