package com.kewen.framework.auth.sample.controller;

import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.core.annotation.data.AuthCheckDataOperation;
import com.kewen.framework.auth.core.annotation.data.AuthDataRange;
import com.kewen.framework.auth.core.annotation.data.AuthEditDataAuth;
import com.kewen.framework.auth.core.annotation.data.authedit.AuthDataEditBusiness;
import com.kewen.framework.auth.core.annotation.data.edit.BusinessData;
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
    @AuthDataRange(module = "testauth")
    @GetMapping("/dataRange")
    public String testDataRange() {
        //直接测试菜单的权限就知道了，
        List<TestauthAnnotationBusiness> list = testauthAnnotationBusinessMpService.list();
        System.out.println(list);
        Assert.isTrue(list.size()==1, "菜单列表为空");
        return "testDataRange";
    }

    /**
     * 测试数据编辑
     * @return
     */
    @PostMapping("/dataEdit")
    @AuthCheckDataOperation(module = "testedit")
    public String testDataEdit(@RequestBody EditBusinessData editBusinessData) {
        System.out.println("successEdit");
        return "testDataEdit";
    }

    /**
     * 测试数据权限编辑
     * @return
     */
    @PostMapping("/dataAuthEdit")
    @AuthEditDataAuth(module = "testauth")
    public String testDataAuthEdit(@RequestBody EditAuthDataEditBusiness applicationBusiness) {

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
    public static class EditBusinessData implements BusinessData {

        private Long id;

        @Override
        public Long getDataId() {
            return id;
        }
    }
    @Data
    public static class EditAuthDataEditBusiness implements AuthDataEditBusiness<Long> {

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
