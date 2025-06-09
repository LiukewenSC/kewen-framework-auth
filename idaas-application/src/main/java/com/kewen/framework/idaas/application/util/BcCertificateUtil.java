package com.kewen.framework.idaas.application.util;


import com.kewen.framework.idaas.application.model.CertificateReq;
import com.kewen.framework.idaas.application.model.CertificateResp;
import com.kewen.framework.idaas.application.saml.SamlException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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

    public static Pair<KeyPair, X509Certificate> generate(CertificateReq certificateReq) {
        try {

            //生成秘钥
            KeyPair keyPair = getKeyPair();

            // 序列号
            BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

            // 签名算法
            ContentSigner signer = new JcaContentSignerBuilder(certificateReq.getSignatureAlgorithm()).build(keyPair.getPrivate());

            // 颁发者
            X500Name issuer = new X500Name(certificateReq.getIssuer());

            //定义有效期
            Date notBefore = certificateReq.getNotBefore();
            Date notAfter = certificateReq.getNotAfter();

            // 定义证书主体（自签名）
            X500Name subject = new X500Name(certificateReq.getSubject());

            // 构建生成证书
            X509Certificate x509Certificate = getX509Certificate(
                    issuer, serial, notBefore, notAfter, subject, keyPair.getPublic(), signer
            );

            // 验证证书
            x509Certificate.verify(keyPair.getPublic());

            return Pair.of(keyPair, x509Certificate);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (OperatorCreationException e) {
            throw new SamlException("OperatorCreationException", e);
        } catch (CertificateException e) {
            throw new SamlException("CertificateException", e);
        } catch (InvalidKeyException e) {
            throw new SamlException("InvalidKeyException", e);
        } catch (NoSuchProviderException e) {
            throw new SamlException("NoSuchProviderException", e);
        } catch (SignatureException e) {
            throw new SamlException("SignatureException", e);
        }
    }

    public static CertificateResp getCertificateResp(KeyPair keyPair, X509Certificate x509Certificate) {
        byte[] encoded = null;
        try {
            encoded = x509Certificate.getEncoded();
        } catch (CertificateEncodingException e) {
            throw new SamlException("x509Certificate.getEncoded error : " + e.getMessage(), e);
        }

        String privateKeyBase64 = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
        String publicKeyBase64 = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
        //TIP 在处理没有中文或其他非ASCII字符的情况时，newStringUsAscii 和 newStringUtf8 的结果通常是相同的，
        // 因为 ASCII 字符集是 UTF-8 字符集的一个子集。不过，这两个方法的具体行为取决于它们是如何定义和使用的。
        //newStringUsAscii 方法:
        //
        //使用 US-ASCII 编码将字节数组转换为字符串。
        //US-ASCII 编码只支持 7 位字符 (0-127)，对于超过 127 的值会被截断或替换为默认字符。
        //newStringUtf8 方法:
        //
        //使用 UTF-8 编码将字节数组转换为字符串。
        //UTF-8 编码可以表示所有 Unicode 字符，但对于纯 ASCII 字符串来说，其表现与 US-ASCII 相同。
        // encodeBase64Chunked 表示可以换行
        //String strCertData = StringUtils.newStringUsAscii(Base64.encodeBase64Chunked(encoded));
        String strCertData = Base64.encodeBase64String(encoded);

        System.out.println("strCertData: " + strCertData);
        System.out.println("privateKeyBase64: " + privateKeyBase64);
        System.out.println("publicKeyBase64: " + publicKeyBase64);

        System.out.println("x509Certificate: " + x509Certificate);
        System.out.println("x509Certificate.getPublicKey(): " + x509Certificate.getPublicKey());

        System.out.println("Certificate generated successfully!");

        return new CertificateResp()
                .setCertData(strCertData)
                .setPrivateKey(privateKeyBase64)
                .setPublicKey(publicKeyBase64);
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
        //keyPairGenerator.initialize(2048, JCAUtil.getSecureRandom());
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        //rsaKeyPairGenerator.initialize(2048, SecureRandom.getInstance("SHA1PRNG"));
        //KeyPair keyPair1 = rsaKeyPairGenerator.generateKeyPair();
        return keyPair;
    }


}
