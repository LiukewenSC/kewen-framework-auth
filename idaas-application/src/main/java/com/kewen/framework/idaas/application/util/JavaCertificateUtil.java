package com.kewen.framework.idaas.application.util;

import com.kewen.framework.idaas.application.model.CertificateReq;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.saml.SamlException;
import org.apache.commons.codec.binary.Base64;
import sun.security.x509.*;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

/**
 * 2025/04/18
 *
 * @author kewen
 * @since 1.0.0
 */
public class JavaCertificateUtil {

    public static final String X509_TYPE = "X.509";
    public static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERTIFICATE = "-----END CERTIFICATE-----";

    public static X509Certificate getCertificate(String certData) throws SamlException {
        try (InputStream inputStream = new ByteArrayInputStream(getFullCertData(certData).getBytes())) {
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

    public static String getFullCertData(String certData) {
        StringWriter buff = new StringWriter();
        PrintWriter out = new PrintWriter(buff);
        out.println(BEGIN_CERTIFICATE);
        out.println(certData);
        out.println(END_CERTIFICATE);
        out.close();
        return buff.toString();
    }

    /**
     * 解析 PKCS#8格式的DER编码的私钥
     *
     * @param base64DerPrivateKey
     * @return
     * @throws Exception
     */
    public static PrivateKey parseDerPrivateKey(String base64DerPrivateKey) {
        byte[] pkcs8Bytes = Base64.decodeBase64(base64DerPrivateKey);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // 或 "EC"，视情况而定

            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SamlException("PrivateKey Exception : " + e.getMessage(), e);
        }
    }

    /**
     * Java内置算法生层证书
     *
     * @param certificateReq
     * @return
     */
    public static CertificateInfo generate(CertificateReq certificateReq) {
        try {
            // 生成RSA密钥对
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 创建X.509证书
            X509CertInfo x509CertInfo = new X509CertInfo();

            // 设置版本号
            x509CertInfo.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));

            // 设置序列号
            BigInteger serialNumber = new BigInteger(Long.toString(System.currentTimeMillis()));
            x509CertInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(serialNumber));

            // 设置算法
            x509CertInfo.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(AlgorithmId.get("RSA")));

            // 设置颁发者
            X500Name issuer = new X500Name(certificateReq.getIssuer());
            x509CertInfo.set(X509CertInfo.ISSUER, new CertificateIssuerName(issuer));


            // 设置有效期
            Date notBefore = certificateReq.getNotBefore();
            Date notAfter = certificateReq.getNotAfter();
            x509CertInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(notBefore, notAfter));

            // 设置主体名
            X500Name owner = new X500Name(certificateReq.getSubject());
            x509CertInfo.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));

            // 设置公钥
            x509CertInfo.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));

            // 创建最终的X.509证书
            X509Certificate x509Certificate = new X509CertImpl(x509CertInfo);
            // 验证证书
            x509Certificate.verify(publicKey);

            //X500PrivateCredential x500PrivateCredential = new X500PrivateCredential(x509Certificate, keyPair.getPrivate());
            return new CertificateInfo(keyPair, x509Certificate);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new SamlException("CertificateException", e);
        } catch (InvalidKeyException e) {
            throw new SamlException("InvalidKeyException", e);
        } catch (NoSuchProviderException e) {
            throw new SamlException("NoSuchProviderException", e);
        } catch (SignatureException e) {
            throw new SamlException("SignatureException", e);
        } catch (IOException e) {
            throw new SamlException("IOException", e);
        }
    }
}