package com.kewen.framework.idaas.application.util;


import com.kewen.framework.idaas.application.model.CertificateReq;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.saml.SamlException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
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

    public static CertificateInfo generate(CertificateReq certificateReq) {
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
            return new CertificateInfo(keyPair, x509Certificate);

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
