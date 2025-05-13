package com.kewen.framework.idaas.application.controller;


import org.opensaml.core.config.ConfigurationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistry;
import org.opensaml.core.xml.io.Marshaller;
import org.opensaml.core.xml.io.MarshallerFactory;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.metadata.*;
import org.opensaml.saml.saml2.metadata.impl.*;
import org.opensaml.security.credential.UsageType;
import org.opensaml.security.x509.BasicX509Credential;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.opensaml.xmlsec.signature.X509Certificate;
import org.opensaml.xmlsec.signature.X509Data;
import org.opensaml.xmlsec.signature.impl.KeyInfoBuilder;
import org.opensaml.xmlsec.signature.impl.X509CertificateBuilder;
import org.opensaml.xmlsec.signature.impl.X509DataBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;

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
public class ApplicationEndpoint {
    private static final Logger log = LoggerFactory.getLogger(ApplicationEndpoint.class);

    private static CertificateGenerator.CertificateResp certificateResp = new CertificateGenerator.CertificateResp();



    @GetMapping("/goSso")
    public void goSso(HttpServletResponse response, HttpServletRequest request) {
        String privateKey = certificateResp.getPrivateKey();
        String publicKey = certificateResp.getPublicKey();
        /*BasicX509Credential credential = new BasicX509Credential(

        );*/
    }

    @GetMapping("/generateCertificate")
    @ResponseBody
    public CertificateGenerator.CertificateResp generateCertificate() {
        CertificateGenerator.CertificateReq certificateReq = new CertificateGenerator.CertificateReq();
        certificateReq.setSubject("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setIssuer("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24))
                .setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)))
        ;
        CertificateGenerator.CertificateResp generate = CertificateGenerator.generate(certificateReq);
        certificateResp = generate;

        return generate;
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

        X509Data x509Data = new X509DataBuilder().buildObject();

        X509Certificate x509Certificate = new X509CertificateBuilder().buildObject();
        //这里是证书
        x509Certificate.setValue(certificateResp.getCertData());
        x509Data.getX509Certificates().add(x509Certificate);

        KeyInfo keyInfo = new KeyInfoBuilder().buildObject();
        keyInfo.getX509Datas().add(x509Data);

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


        MarshallerFactory marshallerFactory = ConfigurationService.get(XMLObjectProviderRegistry.class).getMarshallerFactory();
        Marshaller marshaller = marshallerFactory.getMarshaller(entityDescriptor);
        try {
            marshaller.marshall(entityDescriptor);
            String formatXml = formatXMLObject(entityDescriptor);
            log.info(formatXml);
            return formatXml;
        } catch (MarshallingException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatXMLObject(XMLObject xmlObject) throws TransformerException, MarshallingException {

        TransformerFactory transf = TransformerFactory.newInstance();
        Transformer trans = transf.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        // create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(xmlObject.getDOM());
        trans.transform(source, result);
        return sw.toString();
    }
}
