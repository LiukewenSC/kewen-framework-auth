package com.kewen.framework.idaas.application.controller;


import com.kewen.framework.idaas.application.model.certificate.CertificateGen;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfoStr;
import com.kewen.framework.idaas.application.model.req.IdaasCertificateReq;
import com.kewen.framework.idaas.application.model.resp.CertificateResp;
import com.kewen.framework.idaas.application.service.CertificateService;
import com.kewen.framework.idaas.application.util.JavaCertificateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
@RequestMapping("/certificate")
public class CertificateController {

    private static final Logger log = LoggerFactory.getLogger(CertificateController.class);

    @Autowired
    private CertificateService certificateService;


    @GetMapping("/generateCertificate")
    @ResponseBody
    public CertificateResp generateCertificate(IdaasCertificateReq req) {
        CertificateGen certificateGen = new CertificateGen();
        certificateGen.setSubject("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setIssuer("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setNotBefore(new Date(System.currentTimeMillis()))
                .setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)))
        ;
        CertificateResp certificateInfoStr = certificateService.saveCertificate(req);
        return certificateInfoStr;
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

        return certificateService.getMetadata(id);

    }

    /**
     * 导出证书
     *
     * @param id
     * @param response
     * @throws IOException
     */
    @GetMapping("/export")
    public void exportCertificate(Long id, HttpServletResponse response) throws IOException {

        CertificateResp certificate = certificateService.getCertificate(id);

        JavaCertificateUtil.exportPkcs12Certificate(certificate.getCertificateInfoStr().parseCertificateInfo(), "123456", response.getOutputStream());
    }


    /**
     * 导入证书
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/import")
    @ResponseBody
    public CertificateInfoStr importCertificate(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            CertificateInfo certificateInfo = JavaCertificateUtil.parsePkcs12Certificate(
                    inputStream, "123456", null, null
            );

            CertificateInfoStr certificateInfoStr = new CertificateInfoStr(certificateInfo);

            return certificateInfoStr;

        }
    }
}
