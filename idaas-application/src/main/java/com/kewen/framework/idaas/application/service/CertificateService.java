package com.kewen.framework.idaas.application.service;

import com.kewen.framework.idaas.application.model.certificate.CertificateGen;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfoStr;
import com.kewen.framework.idaas.application.model.req.IdaasCertificateReq;
import com.kewen.framework.idaas.application.model.resp.CertificateResp;
import com.kewen.framework.idaas.application.mp.entity.IdaasCertificate;
import com.kewen.framework.idaas.application.mp.service.IdaasCertificateMpService;
import com.kewen.framework.idaas.application.saml.OpenSAMLUtils;
import com.kewen.framework.idaas.application.saml.SamlXmlUtil;
import com.kewen.framework.idaas.application.util.BcCertificateUtil;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.metadata.*;
import org.opensaml.saml.saml2.metadata.impl.*;
import org.opensaml.security.credential.UsageType;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 证书服务
 *
 * @author kewen
 * @since 2025-06-09
 */
@Component
public class CertificateService {

    @Autowired
    private IdaasCertificateMpService idaasCertificateMpService;


    /**
     * 保存证书
     * @param req
     * @return
     */
    public CertificateResp saveCertificate(IdaasCertificateReq req) {
        CertificateInfo certificateInfo = BcCertificateUtil.generate(
                new CertificateGen()
                        .setSubject(req.getSubject())
                        .setIssuer(req.getIssuer())
                        .setNotBefore(req.getNotBefore())
                        .setNotAfter(req.getNotAfter())
                        .setSignatureAlgorithm(req.getSignatureAlgorithm())
        );
        IdaasCertificate certificate = new IdaasCertificate()
                .setIssuer(req.getIssuer())
                .setSubject(req.getSubject())
                .setSignatureAlgorithm(req.getSignatureAlgorithm())
                .setCertificate(certificateInfo.getCertDataStr())
                .setPrivateKey(certificateInfo.getPrivateKeyStr())
                .setPublicKey(certificateInfo.getPublicKeyStr())
                .setEffectTime(req.getNotBefore())
                .setExpireTime(req.getNotAfter());
        idaasCertificateMpService.save(certificate);
        CertificateInfoStr certificateInfoStr = new CertificateInfoStr(certificateInfo);
        return new CertificateResp()
                .setId(certificate.getId())
                .setIssuer(certificate.getIssuer())
                .setSubject(certificate.getSubject())
                .setEffectTime(certificate.getEffectTime())
                .setExpireTime(certificate.getExpireTime())
                .setCertificateInfoStr(certificateInfoStr);
    }

    /**
     * 获取证书
     * @param id
     * @return
     */
    public CertificateResp getCertificate(Long id) {
        IdaasCertificate certificate = idaasCertificateMpService.getById(id);

        CertificateInfoStr certificateInfoStr = new CertificateInfoStr()
                .setX509CertificateDerStr(certificate.getCertificate())
                .setPrivateKeyBase64Str(certificate.getPrivateKey())
                .setPublicKeyBase64Str(certificate.getPublicKey());
        return new CertificateResp()
                .setId(certificate.getId())
                .setIssuer(certificate.getIssuer())
                .setSubject(certificate.getSubject())
                .setEffectTime(certificate.getEffectTime())
                .setExpireTime(certificate.getExpireTime())
                .setCertificateInfoStr(certificateInfoStr);
    }

    public String getMetadata(Long id) {
        CertificateResp certificate = getCertificate(id);
        return getMetadata(certificate.getCertificateInfoStr().parseCertificateInfo());
    }

    /**
     * 生成Metadata
     *
     * <md:EntityDescriptor xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata" entityID="kewen-idp">
     *     <md:IDPSSODescriptor WantAuthnRequestsSigned="false" protocolSupportEnumeration="urn:oasis:names:tc:SAML:2.0:protocol">
     *         <md:KeyDescriptor use="signing">
     *             <ds:KeyInfo xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
     *                 <ds:X509Data>
     *                     <ds:X509Certificate>MIIDEjCCA......WdYhuQ==</ds:X509Certificate>
     *                 </ds:X509Data>
     *             </ds:KeyInfo>
     *         </md:KeyDescriptor>
     *         <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="http://localhost:8080/webprofile-ref-project/idp/singleLogoutService"/>
     *         <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="http://localhost:8080/webprofile-ref-project/idp/singleLogoutService"/>
     *         <md:SingleSignOnService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="http://localhost:8080/webprofile-ref-project/idp/singleSignOnService"/>
     *         <md:SingleSignOnService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="http://localhost:8080/webprofile-ref-project/idp/singleSignOnServicePost"/>
     *     </md:IDPSSODescriptor>
     * </md:EntityDescriptor>
     * @param certificateInfo
     * @return
     */
    public String getMetadata(CertificateInfo certificateInfo) {
        EntityDescriptor entityDescriptor = new EntityDescriptorBuilder().buildObject();
        entityDescriptor.setEntityID("kewen-idp");

        IDPSSODescriptor idpssoDescriptor = new IDPSSODescriptorBuilder().buildObject();
        idpssoDescriptor.setWantAuthnRequestsSigned(false);
        idpssoDescriptor.addSupportedProtocol(SAMLConstants.SAML20P_NS);

        KeyDescriptor keyDescriptor = new KeyDescriptorBuilder().buildObject();
        keyDescriptor.setUse(UsageType.SIGNING);

        //KeyInfo keyInfo = SamlXmlUtil.getKeyInfo(certificate.getCertData());
        KeyInfo keyInfo = SamlXmlUtil.getKeyInfo(certificateInfo);
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
