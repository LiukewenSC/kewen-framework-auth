package com.kewen.framework.idaas.application.service;

import com.kewen.framework.idaas.application.model.CertificateReq;
import com.kewen.framework.idaas.application.model.CertificateResp;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.mp.entity.IdaasCertificate;
import com.kewen.framework.idaas.application.mp.service.IdaasCertificateMpService;
import com.kewen.framework.idaas.application.util.BcCertificateUtil;
import com.kewen.framework.idaas.application.util.OpenSAMLUtils;
import com.kewen.framework.idaas.application.util.SamlXmlUtil;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml.saml2.metadata.SingleLogoutService;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;
import org.opensaml.saml.saml2.metadata.impl.EntityDescriptorBuilder;
import org.opensaml.saml.saml2.metadata.impl.IDPSSODescriptorBuilder;
import org.opensaml.saml.saml2.metadata.impl.KeyDescriptorBuilder;
import org.opensaml.saml.saml2.metadata.impl.SingleLogoutServiceBuilder;
import org.opensaml.saml.saml2.metadata.impl.SingleSignOnServiceBuilder;
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


    public CertificateResp saveCertificate(CertificateReq req) {

        CertificateInfo certificateInfo = BcCertificateUtil.generate(req);
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
        return new CertificateResp(certificate.getId(), certificateInfo);
    }

    public CertificateResp getCertificate(Long id) {
        IdaasCertificate certificate = idaasCertificateMpService.getById(id);

        return new CertificateResp()
                .setId(certificate.getId())
                .setCertData(certificate.getCertificate())
                .setPrivateKey(certificate.getPrivateKey())
                .setPublicKey(certificate.getPublicKey());
    }

    public String getMetadata(Long id) {
        CertificateResp certificate = getCertificate(id);
        return getMetadata(certificate);
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
     * @param certificate
     * @return
     */
    public String getMetadata(CertificateResp certificate) {
        EntityDescriptor entityDescriptor = new EntityDescriptorBuilder().buildObject();
        entityDescriptor.setEntityID("kewen-idp");

        IDPSSODescriptor idpssoDescriptor = new IDPSSODescriptorBuilder().buildObject();
        idpssoDescriptor.setWantAuthnRequestsSigned(false);
        idpssoDescriptor.addSupportedProtocol(SAMLConstants.SAML20P_NS);

        KeyDescriptor keyDescriptor = new KeyDescriptorBuilder().buildObject();
        keyDescriptor.setUse(UsageType.SIGNING);

        //KeyInfo keyInfo = SamlXmlUtil.getKeyInfo(certificate.getCertData());
        CertificateInfo certificateInfo = certificate.parseCertificateInfo();
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
