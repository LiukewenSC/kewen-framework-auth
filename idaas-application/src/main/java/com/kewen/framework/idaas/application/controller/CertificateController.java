package com.kewen.framework.idaas.application.controller;


import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfoStr;
import com.kewen.framework.idaas.application.model.req.IdaasCertificateReq;
import com.kewen.framework.idaas.application.model.resp.CertificateResp;
import com.kewen.framework.idaas.application.service.CertificateService;
import com.kewen.framework.idaas.application.util.HutoolCertificateUtil;
import com.kewen.framework.idaas.application.util.JavaCertificateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

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
        CertificateResp certificateInfoStr = certificateService.saveCertificate(req);
        return certificateInfoStr;
    }

    @GetMapping("/getCertificate")
    @ResponseBody
    public CertificateResp getCertificate(@RequestParam("id") Long id) {

        return certificateService.getCertificate(id);

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
    @PostMapping("/exportPkcs12")
    public void exportCertificate(@RequestParam("id") Long id, HttpServletResponse response, @RequestParam("password") String password) {

        CertificateResp certificate = certificateService.getCertificate(id);

        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {
            JavaCertificateUtil.exportPkcs12Certificate(certificate.getCertificateInfoStr().parseCertificateInfo(), password, servletOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 导入证书
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/importPkcs12")
    @ResponseBody
    public CertificateInfoStr importCertificate(@RequestParam("file") MultipartFile file, @RequestParam("password") String password) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            CertificateInfo certificateInfo = JavaCertificateUtil.parsePkcs12Certificate(inputStream, password);

            CertificateInfoStr certificateInfoStr = new CertificateInfoStr(certificateInfo);

            return certificateInfoStr;

        }
    }


    /**
     * 导出证书
     *
     * @param id
     * @param type     PRIVATE PUBLIC CERTIFICATE
     * @param response
     * @throws IOException
     */
    @PostMapping("/exportPem")
    public void exportPem(@RequestParam("id") Long id, @RequestParam("type") String type, HttpServletResponse response) {

        CertificateResp certificate = certificateService.getCertificate(id);

        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {
            JavaCertificateUtil.exportPem(certificate.getCertificateInfoStr().parseCertificateInfo(), servletOutputStream, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 导入证书
     *
     * @param file
     * @param type PRIVATE PUBLIC CERTIFICATE
     * @return
     * @throws IOException
     */
    @PostMapping("/importPem")
    @ResponseBody
    public CertificateInfoStr importPem(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            CertificateInfo certificateInfo = JavaCertificateUtil.importPem(inputStream, type);

            CertificateInfoStr certificateInfoStr = new CertificateInfoStr(certificateInfo);

            return certificateInfoStr;

        }
    }


    /**
     * 导出证书
     *
     * @param id
     * @param type     PRIVATE PUBLIC CERTIFICATE
     * @param response
     * @throws IOException
     */
    @PostMapping("/exportPemHutool")
    public void exportPem2(@RequestParam("id") Long id, @RequestParam("type") String type, HttpServletResponse response) {

        CertificateResp certificate = certificateService.getCertificate(id);

        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {
            HutoolCertificateUtil.exportPem(certificate.getCertificateInfoStr(), type, servletOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
