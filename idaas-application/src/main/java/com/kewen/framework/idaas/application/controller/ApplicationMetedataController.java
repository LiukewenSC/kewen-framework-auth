package com.kewen.framework.idaas.application.controller;


import com.kewen.framework.idaas.application.model.CertificateReq;
import com.kewen.framework.idaas.application.model.CertificateResp;
import com.kewen.framework.idaas.application.util.BcCertificateUtil;
import com.kewen.framework.idaas.application.util.OpenSAMLUtils;
import com.kewen.framework.idaas.application.util.SamlCertificateUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.metadata.*;
import org.opensaml.saml.saml2.metadata.impl.*;
import org.opensaml.security.credential.UsageType;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyPair;
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

    private static CertificateResp certificateResp = new CertificateResp();


    @GetMapping("/goSso")
    public void goSso(HttpServletResponse response, HttpServletRequest request) {
        String privateKey = certificateResp.getPrivateKey();
        String publicKey = certificateResp.getPublicKey();
        /*BasicX509Credential credential = new BasicX509Credential(

        );*/
    }

    @GetMapping("/generateCertificate")
    @ResponseBody
    public CertificateResp generateCertificate() {
        CertificateReq certificateReq = new CertificateReq();
        certificateReq.setSubject("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setIssuer("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24))
                .setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)))
        ;

        Pair<KeyPair, java.security.cert.X509Certificate> generate = BcCertificateUtil.generate(certificateReq);
        CertificateResp certificateResp = BcCertificateUtil.getCertificateResp(
                generate.getLeft(), generate.getRight()
        );
        return certificateResp;
    }

    @GetMapping("/generateMetadataBytes")
    public void generateMetadata(HttpServletResponse response) {
        String metadata = generateMetadata();
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(metadata);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/generateMetadata")
    @ResponseBody
    public String generateMetadata() {

        generateCertificate();

        EntityDescriptor entityDescriptor = new EntityDescriptorBuilder().buildObject();
        entityDescriptor.setEntityID("kewen-idp");

        IDPSSODescriptor idpssoDescriptor = new IDPSSODescriptorBuilder().buildObject();
        idpssoDescriptor.setWantAuthnRequestsSigned(false);
        idpssoDescriptor.addSupportedProtocol(SAMLConstants.SAML20P_NS);

        KeyDescriptor keyDescriptor = new KeyDescriptorBuilder().buildObject();
        keyDescriptor.setUse(UsageType.SIGNING);

        KeyInfo keyInfo = SamlCertificateUtil.getKeyInfo(certificateResp.getCertData());

        keyDescriptor.setKeyInfo(keyInfo);

        idpssoDescriptor.getKeyDescriptors().add(keyDescriptor);


        SingleSignOnService singleSignOnService = new SingleSignOnServiceBuilder().buildObject();
        singleSignOnService.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
        singleSignOnService.setLocation("http://localhost:8080/webprofile-ref-project/idp/singleSignOnService");
        idpssoDescriptor.getSingleSignOnServices().add(singleSignOnService);

        SingleSignOnService singleSignOnServicePost = new SingleSignOnServiceBuilder().buildObject();
        singleSignOnServicePost.setBinding(SAMLConstants.SAML2_POST_BINDING_URI);
        singleSignOnServicePost.setLocation("http://localhost:8080/webprofile-ref-project/idp/singleSignOnServicePost");
        idpssoDescriptor.getSingleSignOnServices().add(singleSignOnServicePost);


        SingleLogoutService singleLogoutService = new SingleLogoutServiceBuilder().buildObject();
        singleLogoutService.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
        singleLogoutService.setLocation("http://localhost:8080/webprofile-ref-project/idp/singleLogoutService");
        idpssoDescriptor.getSingleLogoutServices().add(singleLogoutService);

        SingleLogoutService singleLogoutServicePost = new SingleLogoutServiceBuilder().buildObject();
        singleLogoutServicePost.setBinding(SAMLConstants.SAML2_POST_BINDING_URI);
        singleLogoutServicePost.setLocation("http://localhost:8080/webprofile-ref-project/idp/singleLogoutService");
        idpssoDescriptor.getSingleLogoutServices().add(singleLogoutServicePost);

        entityDescriptor.getRoleDescriptors().add(idpssoDescriptor);
        OpenSAMLUtils.marshall(entityDescriptor);
        return OpenSAMLUtils.formatXMLObject(entityDescriptor.getDOM());

    }


}
