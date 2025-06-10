package com.kewen.framework.idaas.application.service;

import com.kewen.framework.idaas.application.model.CertificateReq;
import com.kewen.framework.idaas.application.model.CertificateResp;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.mp.entity.IdaasCertificate;
import com.kewen.framework.idaas.application.mp.service.IdaasCertificateMpService;
import com.kewen.framework.idaas.application.util.BcCertificateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    public CertificateResp saveCertificate(CertificateReq req) {

        CertificateInfo certificateInfo = BcCertificateUtil.generate(req);
        IdaasCertificate certificate = new IdaasCertificate()
                .setIssuer(req.getIssuer())
                .setSubject(req.getSubject())
                .setSignatureAlgorithm(req.getSignatureAlgorithm())
                .setCertificate(certificateInfo.getCertDataStr())
                .setPrivateKey(certificateInfo.getPrivateKeyStr())
                .setPublicKey(certificateInfo.getPublicKeyStr())
                .setEffectTime(req.getNotBefore())
                .setExpireTime(req.getNotAfter());
        idaasCertificateMpService.save(certificate);
        return new CertificateResp(certificate.getId(), certificateInfo);
    }

    public CertificateResp getCertificate(Long id) {
        IdaasCertificate certificate = idaasCertificateMpService.getById(id);

        return new CertificateResp()
                .setId(certificate.getId())
                .setCertData(certificate.getCertificate())
                .setPrivateKey(certificate.getPrivateKey())
                .setPublicKey(certificate.getPublicKey());
    }
}
