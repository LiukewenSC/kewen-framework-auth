package com.kewen.framework.idaas.application.saml;


import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2025/04/13
 * 阿里云角色登录
 *
 * @author kewen
 * @since 1.0.0
 */
@Controller
public class AliyunRoleSso {

    public void gotoAliyunRoleSso(HttpServletRequest request, HttpServletResponse response) {
        // 跳转到阿里云
        response.setStatus(302);
        response.setHeader("Location", "https://signin.aliyun.com");


    }

}
