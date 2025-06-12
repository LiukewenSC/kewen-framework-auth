package com.kewen.framework.idaas.application.controller;


import com.kewen.framework.idaas.application.model.CertificateReq;
import com.kewen.framework.idaas.application.model.CertificateResp;
import com.kewen.framework.idaas.application.service.CertificateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 2025/04/13
 * <md:EntityDescriptor xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata" entityID="kewen-idp">
 * <md:IDPSSODescriptor WantAuthnRequestsSigned="false" protocolSupportEnumeration="urn:oasis:names:tc:SAML:2.0:protocol">
 * <md:KeyDescriptor use="signing">
 * <ds:KeyInfo xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
 * <ds:X509Data>
 * <ds:X509Certificate>certData</ds:X509Certificate>
 * </ds:X509Data>
 * </ds:KeyInfo>
 * </md:KeyDescriptor>
 * <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="http://localhost:8080/webprofile-ref-project/idp/singleLogoutService"/>
 * <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="http://localhost:8080/webprofile-ref-project/idp/singleLogoutService"/>
 * <md:SingleSignOnService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="http://localhost:8080/webprofile-ref-project/idp/singleSignOnService"/>
 * <md:SingleSignOnService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="http://localhost:8080/webprofile-ref-project/idp/singleSignOnServicePost"/>
 * </md:IDPSSODescriptor>
 * </md:EntityDescriptor>
 *
 * @author kewen
 * @since 1.0.0
 */
@Controller
@RequestMapping("/application")
public class ApplicationMetedataController {

    private static final Logger log = LoggerFactory.getLogger(ApplicationMetedataController.class);

    @Autowired
    private CertificateService certificateService;


    @GetMapping("/generateCertificate")
    @ResponseBody
    public CertificateResp generateCertificate() {
        CertificateReq certificateReq = new CertificateReq();
        certificateReq.setSubject("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setIssuer("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setNotBefore(new Date(System.currentTimeMillis()))
                .setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)))
        ;
        CertificateResp certificateResp = certificateService.saveCertificate(certificateReq);
        return certificateResp;
    }

    @GetMapping("/getMetadataBytes")
    public void getMetadataBytes(@RequestParam("id") Long id, HttpServletResponse response) {
        String metadata = certificateService.getMetadata(id);
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(metadata);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成Metadata
     * @param id
     * @return
     */
    @GetMapping("/getMetadata")
    @ResponseBody
    public String getMetadata(@RequestParam("id") Long id) {

        CertificateResp certificate = certificateService.getCertificate(id);

        return certificateService.getMetadata(certificate);

    }


}
