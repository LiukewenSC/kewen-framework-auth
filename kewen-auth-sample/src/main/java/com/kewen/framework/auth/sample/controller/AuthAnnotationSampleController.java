package com.kewen.framework.auth.sample.controller;

import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.core.annotation.data.AuthCheckDataOperation;
import com.kewen.framework.auth.core.annotation.data.AuthDataRange;
import com.kewen.framework.auth.core.annotation.data.AuthEditDataAuth;
import com.kewen.framework.auth.core.annotation.data.authedit.IdDataAuthEdit;
import com.kewen.framework.auth.core.annotation.data.edit.IdDataEdit;
import com.kewen.framework.auth.core.annotation.menu.AuthCheckMenuAccess;
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
public class AuthAnnotationSampleController {

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
    @AuthCheckMenuAccess
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
