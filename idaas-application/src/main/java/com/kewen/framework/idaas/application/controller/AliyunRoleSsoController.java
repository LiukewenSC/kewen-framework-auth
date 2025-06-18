/*
 * Copyright (c) 2019 BeiJing JZYT Technology Co. Ltd
 * www.idsmanager.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * BeiJing JZYT Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with BeiJing JZYT Technology Co. Ltd.
 */

package com.kewen.framework.idaas.application.controller;

import com.kewen.framework.idaas.application.model.resp.CertificateResp;
import com.kewen.framework.idaas.application.model.saml.AliyunRoleSsoXmlResponse;
import com.kewen.framework.idaas.application.saml.ResponseUtil;
import com.kewen.framework.idaas.application.service.CertificateService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 2025/05/13
 *
 * @author kewen
 */
@RequestMapping("/sso/role/aliyun")
@Controller
public class AliyunRoleSsoController {

    private static final Logger log = LoggerFactory.getLogger(AliyunRoleSsoController.class);


    @Autowired
    private CertificateService certificateService;

    /**
     * 跳转阿里云登录
     *
     * @param id
     * @param httpServletResponse
     */
    @GetMapping("/go")
    @ResponseBody
    public void goSso(@RequestParam("id") Long id, HttpServletResponse httpServletResponse) {

        CertificateResp certificate = certificateService.getCertificate(id);
        //构造阿里云角色SSO对象
        AliyunRoleSsoXmlResponse aliyunRoleSsoXmlResponse = new AliyunRoleSsoXmlResponse(
                "kewen-idp",
                certificate.getCertificateInfoStr(),
                DateTime.now().plusHours(2),
                "acs:ram::1555734646214700:role/kewen-saml-role,acs:ram::1555734646214700:saml-provider/kewen-saml",
                "IDPAdmin"
        );
        ResponseUtil.redirect(httpServletResponse, aliyunRoleSsoXmlResponse);
    }

}
