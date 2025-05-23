package com.kewen.framework.idaas.application.saml.util;

import com.kewen.framework.idaas.application.config.IDPCredentials;
import com.kewen.framework.idaas.application.exception.CertificationException;
import net.shibboleth.utilities.java.support.security.RandomIdentifierGenerationStrategy;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.security.SecurityException;
import org.opensaml.security.credential.BasicCredential;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.xmlsec.keyinfo.KeyInfoGenerator;
import org.opensaml.xmlsec.keyinfo.impl.X509KeyInfoGeneratorFactory;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.X509Certificate;
import org.opensaml.xmlsec.signature.X509Data;
import org.opensaml.xmlsec.signature.impl.*;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.opensaml.xmlsec.signature.support.Signer;

import java.security.PrivateKey;
import java.security.PublicKey;

public class SamlCertificateUtil {


    public static KeyInfo getKeyInfo(String x509CertificateValue) {
        if (x509CertificateValue == null) {
            throw new CertificationException("Certification parameter is null");
        }
        KeyInfo keyInfo = new KeyInfoBuilder().buildObject();
        X509Data x509Data = new X509DataBuilder().buildObject();
        X509Certificate x509Certificate = new X509CertificateBuilder().buildObject();
        x509Certificate.setValue(x509CertificateValue);
        x509Data.getX509Certificates().add(x509Certificate);
        keyInfo.getX509Datas().add(x509Data);
        return keyInfo;
    }

    private KeyInfo getKeyInfo2(String x509CertificateValue) {

        X509KeyInfoGeneratorFactory x509KeyInfoGeneratorFactory = new X509KeyInfoGeneratorFactory();
        x509KeyInfoGeneratorFactory.setEmitEntityCertificate(true);
        KeyInfoGenerator keyInfoGenerator = x509KeyInfoGeneratorFactory.newInstance();

        PublicKey publicKey = null;
        PrivateKey privateKey = null;

        BasicCredential credential = CredentialSupport.getSimpleCredential(publicKey, privateKey);
        KeyInfo keyInfo = null;
        try {
            keyInfo = keyInfoGenerator.generate(credential);
            return keyInfo;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static void signAssertion(Assertion assertion, KeyInfo keyInfo) {
        Signature signature = getSignature(keyInfo);
        assertion.setSignature(signature);
        try {
            //noinspection ConstantConditions =》marshall 要求输入Nonnull且输出为Nonull；
            XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(assertion).marshall(assertion);
        } catch (MarshallingException e) {
            throw new RuntimeException(e);
        }
        try {
            Signer.signObject(signature);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static Signature getSignature(KeyInfo keyInfo) {
        SignatureImpl signature = new SignatureBuilder().buildObject();
        //todo 这里去掉默认的
        signature.setSigningCredential(IDPCredentials.getCredential());
        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        signature.setKeyInfo(keyInfo);
        return signature;
    }

    private static final RandomIdentifierGenerationStrategy secureRandomIdGenerator = new RandomIdentifierGenerationStrategy();

    public static String generateSecureRandomId() {
        return secureRandomIdGenerator.generateIdentifier();
    }

}
