package com.kewen.framework.idaas.application.util;

import com.kewen.framework.idaas.application.exception.CertificationException;
import com.kewen.framework.idaas.application.model.certificate.CertificateGen;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.saml.SamlException;
import org.apache.commons.codec.binary.Base64;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.Enumeration;

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
            throw new CertificationException(e);
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
            throw new CertificationException("PrivateKey Exception : " + e.getMessage(), e);
        }
    }

    /**
     * Java内置算法生层证书
     *
     * @param certificateGen
     * @return
     */
    public static CertificateInfo generate(CertificateGen certificateGen) {
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
            X500Name issuer = new X500Name(certificateGen.getIssuer());
            x509CertInfo.set(X509CertInfo.ISSUER, new CertificateIssuerName(issuer));


            // 设置有效期
            Date notBefore = certificateGen.getNotBefore();
            Date notAfter = certificateGen.getNotAfter();
            x509CertInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(notBefore, notAfter));

            // 设置主体名
            X500Name owner = new X500Name(certificateGen.getSubject());
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
            throw new CertificationException(e);
        } catch (CertificateException e) {
            throw new CertificationException("CertificateException", e);
        } catch (InvalidKeyException e) {
            throw new CertificationException("InvalidKeyException", e);
        } catch (NoSuchProviderException e) {
            throw new CertificationException("NoSuchProviderException", e);
        } catch (SignatureException e) {
            throw new CertificationException("SignatureException", e);
        } catch (IOException e) {
            throw new CertificationException("IOException", e);
        }
    }

    /**
     *
     * @param in PKCS#12 文件
     * @param password PKCS#12 文件密码
     * @return 包含私钥和证书
     * @throws RuntimeException
     */
    public static CertificateInfo parsePkcs12Certificate(InputStream in,
                                                         String password) throws RuntimeException {
        return parsePkcs12Certificate(in, password, null, null);
    }
    /**
     * 从 .p12 文件中加载私钥和证书
     *
     * @param in            PKCS#12 文件路径
     * @param storePassword 密钥库密码（用于打开整个 .p12 文件）
     * @param keyAlias      别名（可选，如果不指定则取第一个可用别名）
     * @param keyPassword   私钥密码（如果与 storePassword 相同可设为 null）
     * @return 包含私钥和证书
     */
    public static CertificateInfo parsePkcs12Certificate(InputStream in,
                                                         String storePassword, String keyAlias, String keyPassword) throws RuntimeException {

        try {
            // Step 1: 加载 PKCS12 KeyStore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            keyStore.load(in, storePassword.toCharArray());

            // Step 2: 获取别名（如果没有指定）
            if (keyAlias == null || keyAlias.isEmpty()) {
                Enumeration<String> aliases = keyStore.aliases();
                while (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();
                    if (keyStore.isKeyEntry(alias)) {
                        keyAlias = alias;
                        break;
                    }
                }
            }

            if (keyAlias == null) {
                throw new CertificationException("未找到有效的密钥条目");
            }

            // Step 3: 获取私钥
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPassword == null ? storePassword.toCharArray() : keyPassword.toCharArray());

            // Step 4: 获取证书
            Certificate cert = keyStore.getCertificate(keyAlias);
            if (!(cert instanceof X509Certificate)) {
                throw new RuntimeException("证书不是 X.509 格式");
            }
            return new CertificateInfo(privateKey, null, ((X509Certificate) cert));
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                 UnrecoverableKeyException e) {
            throw new CertificationException(e);
        }
    }

    /**
     * 导出pkcs12证书，
     *
     * @param certificateInfo 证书信息
     * @param password        密码
     * @param response        不关流
     */
    public static void exportPkcs12Certificate(CertificateInfo certificateInfo, String password, OutputStream response) {
        try {
            KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
            pkcs12.load(null, null);
            X509Certificate certificate = certificateInfo.getCertificate();
            Certificate[] chain = new Certificate[]{certificate};
            char[] passwordCharArray = password.toCharArray();
            pkcs12.setKeyEntry("aliasCertificate", certificateInfo.getKeyPair().getPrivate(), passwordCharArray, chain);
            pkcs12.store(response, passwordCharArray);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new CertificationException(e);
        }
    }
}