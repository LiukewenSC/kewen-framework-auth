package com.kewen.framework.idaas.application.model;

import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.saml.SamlException;
import com.kewen.framework.idaas.application.util.JavaCertificateUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Data
@Accessors(chain = true)
public class CertificateResp {


    public static final String X509_TYPE = "X.509";
    public static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERTIFICATE = "-----END CERTIFICATE-----";

    private Long id;
    private String certData;
    private String privateKey;
    private String publicKey;

    public CertificateResp() {
    }

    public CertificateResp(Long id, CertificateInfo certificateInfo) {
        this.id = id;
        this.certData = certificateInfo.getCertDataStr();
        this.privateKey = certificateInfo.getPrivateKeyStr();
        this.publicKey = certificateInfo.getPublicKeyStr();
    }

    public CertificateInfo parseCertificateInfo() {
        PublicKey publicKey1 = parsePublicKey();
        PrivateKey privateKey1 = parsePrivateKey();
        X509Certificate x509Certificate = parseX509Certificate();
        return new CertificateInfo(new KeyPair(publicKey1, privateKey1), x509Certificate);
    }

    public PrivateKey parsePrivateKey() {
        return JavaCertificateUtil.parseDerPrivateKey(privateKey);
    }

    public PublicKey parsePublicKey() {
        byte[] publicKeyBytes = Base64.decodeBase64(publicKey);

        // 使用 X.509 编码规范加载公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // 或 "EC"，根据密钥类型

            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SamlException("parsePublicKey exception", e);
        }

        /*PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // 或 "EC"，视情况而定
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }*/
    }

    public X509Certificate parseX509Certificate() throws SamlException {
        try (InputStream inputStream = new ByteArrayInputStream(getFullCertData().getBytes())) {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance(X509_TYPE);
            Certificate certificate = certFactory.generateCertificate(inputStream);
            X509Certificate x509Certificate = (X509Certificate) certificate;
            x509Certificate.checkValidity();
            return x509Certificate;
        } catch (IOException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFullCertData() {
        StringWriter buff = new StringWriter();
        PrintWriter out = new PrintWriter(buff);
        out.println(BEGIN_CERTIFICATE);
        out.println(certData);
        out.println(END_CERTIFICATE);
        out.close();
        return buff.toString();
    }
}
