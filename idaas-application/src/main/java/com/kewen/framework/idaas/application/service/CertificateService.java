package com.kewen.framework.idaas.application.service;

import com.kewen.framework.idaas.application.model.CertificateReq;
import com.kewen.framework.idaas.application.model.CertificateResp;
import com.kewen.framework.idaas.application.mp.entity.IdaasCertificate;
import com.kewen.framework.idaas.application.mp.service.IdaasCertificateMpService;
import com.kewen.framework.idaas.application.util.BcCertificateUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

/**
 * 证书服务
 *
 * @author kewen
 * @since 2025-06-09
 */
@Component
public class CertificateService {

    @Autowired
    private IdaasCertificateMpService idaasCertificateMpService;

    public CertificateResp getCertificate(Long id) {
        IdaasCertificate certificate = idaasCertificateMpService.getById(id);

        return new CertificateResp()
                .setId(certificate.getId())
                .setCertData(certificate.getCertificate())
                .setPrivateKey(certificate.getPrivateKey())
                .setPublicKey(certificate.getPublicKey());
    }

    public CertificateResp saveCertificate(CertificateReq req) {


        Pair<KeyPair, X509Certificate> generate = BcCertificateUtil.generate(req);
        CertificateResp resp = BcCertificateUtil.getCertificateResp(
                generate.getLeft(), generate.getRight()
        );

        IdaasCertificate certificate = new IdaasCertificate()
                .setIssuer(req.getIssuer())
                .setSubject(req.getSubject())
                .setSignatureAlgorithm(req.getSignatureAlgorithm())
                .setCertificate(resp.getCertData())
                .setPrivateKey(resp.getPrivateKey())
                .setPublicKey(resp.getPublicKey())
                .setEffectTime(req.getNotBefore())
                .setExpireTime(req.getNotAfter());
        idaasCertificateMpService.save(certificate);
        resp.setId(certificate.getId());
        return resp;
    }

}
