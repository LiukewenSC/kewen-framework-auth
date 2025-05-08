package com.kewen.framework.idaas.application.controller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import sun.security.x509.*;

import javax.security.auth.x500.X500PrivateCredential;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Date;

/**
 * 2025/04/18
 *
 * @author kewen
 * @since 1.0.0
 */
public class CertificateJavaSecurity {

    public static void main(String[] args) throws Exception {
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

        // 设置公钥
        x509CertInfo.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));

        // 设置主体名
        X500Name owner = new X500Name("CN=MyUser,O=MyOrg,L=MyCity,C=MY");
        x509CertInfo.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));

        // 设置颁发者名
        X500Name issuer = new X500Name("CN=MyCA,O=MyOrg,L=MyCity,C=MY");
        x509CertInfo.set(X509CertInfo.ISSUER, new CertificateIssuerName(issuer));

        // 设置有效期
        Date notBefore = Date.from(Instant.now().minusSeconds(5 * 60 * 60));
        Date notAfter = Date.from(Instant.now().plusSeconds(5 * 60 * 60));
        x509CertInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(notBefore, notAfter));


        // 创建最终的X.509证书
        X509Certificate x509Certificate = new X509CertImpl(x509CertInfo);
        // 验证证书
        x509Certificate.verify(publicKey);

        X500PrivateCredential x500PrivateCredential = new X500PrivateCredential(x509Certificate, keyPair.getPrivate());

        byte[] encoded = x509Certificate.getEncoded();
        String strCertData = StringUtils.newStringUtf8(Base64
                .encodeBase64Chunked(encoded));

        // 输出证书信息
        System.out.println(x509Certificate.toString());
    }
}