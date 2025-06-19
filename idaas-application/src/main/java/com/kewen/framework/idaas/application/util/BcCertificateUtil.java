package com.kewen.framework.idaas.application.util;


import com.kewen.framework.idaas.application.exception.CertificationException;
import com.kewen.framework.idaas.application.model.certificate.CertificateGen;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.StringReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Date;

/**
 * 2025/04/18
 *
 * @author kewen
 * @since 1.0.0
 */
public class BcCertificateUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 未测试
     * 从 PKCS#1 格式的 DER 字节数组加载 Java 的 RSAPrivateKey。
     */
    public static PrivateKey loadPkcs1PrivateKey(byte[] pkcs1DerBytes) throws Exception {
        // Step 1: 使用 BouncyCastle 解析 DER 编码的 ASN.1 序列
        ASN1Sequence sequence = ASN1Sequence.getInstance(pkcs1DerBytes);
        org.bouncycastle.asn1.pkcs.RSAPrivateKey bcPrivateKey = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(sequence);

        // Step 2: 提取模数 (modulus) 和私有指数 (private exponent)
        java.math.BigInteger modulus = bcPrivateKey.getModulus();
        java.math.BigInteger privateExponent = bcPrivateKey.getPrivateExponent();

        // Step 3: 构造 Java 标准的 RSAPrivateKeySpec
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, privateExponent);

        // Step 4: 使用 KeyFactory 构建 Java 的 PrivateKey 对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 未测试
     * 加载PEM格式的私钥
     *
     * @param pemPrivateKey
     * @return
     * @throws Exception
     */
    public PrivateKey loadPKCS8PemPrivateKey(String pemPrivateKey) throws Exception {
        PEMParser pemParser = new PEMParser(new StringReader(pemPrivateKey));
        Object object = pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        PrivateKeyInfo privateKeyInfo;

        if (object instanceof PrivateKeyInfo) {
            privateKeyInfo = (PrivateKeyInfo) object;
        } else {
            throw new IllegalArgumentException("Not a valid private key format.");
        }

        return converter.getPrivateKey(privateKeyInfo);
    }



    /**
     * BC库算法生成证书
     *
     * @param certificateGen
     * @return
     */
    public static CertificateInfo generate(CertificateGen certificateGen) {
        try {

            //生成秘钥
            KeyPair keyPair = getKeyPair();

            // 序列号
            BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

            // 签名算法
            ContentSigner signer = new JcaContentSignerBuilder(certificateGen.getSignatureAlgorithm()).build(keyPair.getPrivate());

            // 颁发者
            X500Name issuer = new X500Name(certificateGen.getIssuer());

            //定义有效期
            Date notBefore = certificateGen.getNotBefore();
            Date notAfter = certificateGen.getNotAfter();

            // 定义证书主体（自签名）
            X500Name subject = new X500Name(certificateGen.getSubject());

            // 构建生成证书
            X509Certificate x509Certificate = getX509Certificate(
                    issuer, serial, notBefore, notAfter, subject, keyPair.getPublic(), signer
            );

            // 验证证书
            x509Certificate.verify(keyPair.getPublic());
            return new CertificateInfo(keyPair, x509Certificate);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (OperatorCreationException e) {
            throw new CertificationException("OperatorCreationException", e);
        } catch (CertificateException e) {
            throw new CertificationException("CertificateException", e);
        } catch (InvalidKeyException e) {
            throw new CertificationException("InvalidKeyException", e);
        } catch (NoSuchProviderException e) {
            throw new CertificationException("NoSuchProviderException", e);
        } catch (SignatureException e) {
            throw new CertificationException("SignatureException", e);
        }
    }

    /**
     * 生成 x509证书
     *
     * @param issuer
     * @param serial
     * @param notBefore
     * @param notAfter
     * @param subject
     * @param publicKey
     * @param signer
     * @return
     * @throws CertificateException
     */
    private static X509Certificate getX509Certificate(X500Name issuer, BigInteger serial, Date notBefore, Date notAfter,
                                                      X500Name subject, PublicKey publicKey, ContentSigner signer) throws CertificateException {
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer, serial, notBefore, notAfter, subject, publicKey
        );
        X509CertificateHolder certificateHolder = certBuilder.build(signer);
        X509Certificate x509Certificate = new JcaX509CertificateConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .getCertificate(certificateHolder);
        return x509Certificate;
    }

    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        // 生成密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //JCAUtil.getSecureRandom()
        //SecureRandom.getInstance("SHA1PRNG")
        //keyPairGenerator.initialize(2048, JCAUtil.getSecureRandom());
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        //rsaKeyPairGenerator.init(new KeyGenerationParameters(JCAUtil.getSecureRandom(),2048));
        //AsymmetricCipherKeyPair keyPair1 = rsaKeyPairGenerator.generateKeyPair();
        return keyPair;
    }


}
