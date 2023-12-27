package com.kewen.framework.auth.sample.controller;

import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.annotation.data.authedit.AuthDataEditBusiness;
import com.kewen.framework.auth.core.annotation.data.edit.ApplicationBusiness;
import com.kewen.framework.auth.sample.mp.entity.SysMenu;
import com.kewen.framework.auth.sample.mp.entity.TestauthAnnotationBusiness;
import com.kewen.framework.auth.sample.mp.service.SysMenuMpService;
import com.kewen.framework.auth.core.annotation.data.CheckDataOperation;
import com.kewen.framework.auth.core.annotation.data.DataRange;
import com.kewen.framework.auth.core.annotation.data.EditDataAuth;
import com.kewen.framework.auth.core.annotation.menu.CheckMenuAccess;
import com.kewen.framework.auth.sample.mp.service.TestauthAnnotationBusinessMpService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @DataRange(module = "testauth")
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
    @CheckDataOperation(module = "testedit")
    public String testDataEdit(@RequestBody EditApplicationBusiness applicationBusiness) {
        System.out.println("successEdit");
        return "testDataEdit";
    }

    /**
     * 测试数据权限编辑
     * @return
     */
    @PostMapping("/dataAuthEdit")
    @EditDataAuth(module = "testauth")
    public String testDataAuthEdit(@RequestBody EditAuthDataEditBusiness applicationBusiness) {

        return "testDataAuthEdit";
    }

    /**
     * 测试菜单控制
     * @return
     */
    @CheckMenuAccess
    @GetMapping("/checkMenu")
    public String testCheckMenu() {

        return "testCheckMenu";
    }

    @Data
    public static class EditApplicationBusiness implements ApplicationBusiness {

        private Long id;

        @Override
        public Long getBusinessId() {
            return id;
        }
    }
    @Data
    public static class EditAuthDataEditBusiness implements AuthDataEditBusiness<Long> {

        private Long id;

        @Override
        public Long getBusinessId() {
            return id;
        }

        @Override
        public IAuthObject getAuthObject() {
            return null;
        }

    }


}
