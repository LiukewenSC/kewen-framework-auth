package com.kewen.framework.idaas.application.model.certificate;

import com.kewen.framework.idaas.application.saml.SamlException;
import com.kewen.framework.idaas.application.util.JavaCertificateUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author kewen
 */
@Data
@Accessors(chain = true)
public class CertificateInfoStr {


    public static final String X509_TYPE = "X.509";
    public static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERTIFICATE = "-----END CERTIFICATE-----";
    /**
     * 证书的DER经过Base64后的字符串
     */
    private String x509CertificateDerStr;
    /**
     * 私钥经过Base64后的字符串
     */
    private String privateKeyBase64Str;
    /**
     * 公钥经过Base64后的字符串
     */
    private String publicKeyBase64Str;

    public CertificateInfoStr() {
    }

    public CertificateInfoStr(CertificateInfo certificateInfo) {
        this.x509CertificateDerStr = certificateInfo.getCertDataStr();
        this.privateKeyBase64Str = certificateInfo.getPrivateKeyStr();
        this.publicKeyBase64Str = certificateInfo.getPublicKeyStr();
    }

    public CertificateInfo parseCertificateInfo() {
        PublicKey publicKey1 = parsePublicKey();
        PrivateKey privateKey1 = parsePrivateKey();
        X509Certificate x509Certificate = parseX509Certificate();
        return new CertificateInfo(new KeyPair(publicKey1, privateKey1), x509Certificate);
    }

    public PrivateKey parsePrivateKey() {
        if (StringUtils.isEmpty(privateKeyBase64Str)) {
            return null;
        }
        return JavaCertificateUtil.parseDerPrivateKey(privateKeyBase64Str);
    }

    public PublicKey parsePublicKey() {

        if (StringUtils.isEmpty(publicKeyBase64Str)) {
            return null;
        }

        byte[] publicKeyBytes = Base64.decodeBase64(publicKeyBase64Str);

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

    /**
     *
     *
     * @return
     * @throws SamlException
     */
    public X509Certificate parseX509Certificate() throws SamlException {
        if (StringUtils.isEmpty(x509CertificateDerStr)) {
            return null;
        }
        // 这里实际不需要转换成PEM格式的，直接使用Base64解码成byte[]即可
        //-----BEGIN CERTIFICATE-----
        //<Base64 编码的 DER 数据>
        //-----END CERTIFICATE-----
        //try (InputStream inputStream = new ByteArrayInputStream(getPemCertData().getBytes())) {
        try (InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(x509CertificateDerStr))) {
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

    private String getPemCertData() {
        StringWriter buff = new StringWriter();
        PrintWriter out = new PrintWriter(buff);
        out.println(BEGIN_CERTIFICATE);
        out.println(x509CertificateDerStr);
        out.println(END_CERTIFICATE);
        out.close();
        return buff.toString();
    }
}
