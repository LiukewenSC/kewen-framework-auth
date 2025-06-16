/*
 * Copyright (c) 2019 BeiJing JZYT Technology Co. Ltd
 * www.idsmanager.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * BeiJing JZYT Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with BeiJing JZYT Technology Co. Ltd.
 */

package com.kewen.framework.idaas.application.model.certificate;

import com.kewen.framework.idaas.application.saml.SamlException;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * 2025/06/10
 *
 * @author kewen
 * @since 4.23.0-mysql-aliyun-sovereign
 */
@Getter
public class CertificateInfo {

    /**
     * 公私钥
     */
    private final KeyPair keyPair;

    /**
     * 证书内容
     */
    private final X509Certificate certificate;

    public CertificateInfo(PrivateKey privateKey, PublicKey publicKey, X509Certificate certificate) {
        this.keyPair = new KeyPair(publicKey, privateKey);
        this.certificate = certificate;
    }

    public CertificateInfo(KeyPair keyPair, X509Certificate certificate) {
        this.keyPair = keyPair;
        this.certificate = certificate;
    }

    /**
     * 私钥字符串
     *
     * @return
     */
    public String getPrivateKeyStr() {
        PrivateKey aPrivate = keyPair.getPrivate();
        if (aPrivate == null) {
            return null;
        }
        return Base64.encodeBase64String(aPrivate.getEncoded());
    }

    /**
     * 公钥字符串
     *
     * @return
     */
    public String getPublicKeyStr() {
        PublicKey aPublic = keyPair.getPublic();
        if (aPublic == null) {
            return null;
        }
        return Base64.encodeBase64String(aPublic.getEncoded());
    }

    /**
     * 获取证书数据
     *
     * @return
     */
    public String getCertDataStr() {
        if (certificate == null) {
            return null;
        }
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
        try {
            byte[] encoded = certificate.getEncoded();
            if (encoded == null) {
                return null;
            }
            return Base64.encodeBase64String(encoded);
        } catch (CertificateEncodingException e) {
            throw new SamlException("x509Certificate.getEncoded error : " + e.getMessage(), e);
        }
    }
}
