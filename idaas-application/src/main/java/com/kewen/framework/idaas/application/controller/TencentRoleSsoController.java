package com.kewen.framework.idaas.application.controller;

import com.kewen.framework.idaas.application.model.resp.CertificateResp;
import com.kewen.framework.idaas.application.model.saml.AbstractXmlResponse;
import com.kewen.framework.idaas.application.model.saml.TencentRoleSsoXmlResponse;
import com.kewen.framework.idaas.application.service.CertificateService;
import com.kewen.framework.idaas.application.util.ResponseUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/sso/role/tencent")
@Controller
public class TencentRoleSsoController {

    @Autowired
    private CertificateService certificateService;

    /**
     * 跳转腾讯云登录
     *
     * @param id
     * @param httpServletResponse
     */
    @GetMapping("/go")
    @ResponseBody
    public void goSso(@RequestParam("id") Long id, HttpServletResponse httpServletResponse) {

        CertificateResp certificate = certificateService.getCertificate(id);
        //构造阿里云角色SSO对象
        AbstractXmlResponse aliyunRoleSsoSimpleAttributeXmlResponse = new TencentRoleSsoXmlResponse(
                "kewen-idp",
                "IDPAdmin",
                certificate.getCertificateInfoStr(),
                "qcs::cam::uin/100011725657:roleName/KewenSamlRole,qcs::cam::uin/100011725657:saml-provider/KewenSamlTest",
                DateTime.now().plusHours(2)
        );
        ResponseUtil.redirect(httpServletResponse, aliyunRoleSsoSimpleAttributeXmlResponse);
    }


}
