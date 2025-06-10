package com.kewen.framework.idaas.application.model;

import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CertificateResp {
    private Long id;
    private String certData;
    private String privateKey;
    private String publicKey;

    public CertificateResp() {
    }

    public CertificateResp(Long id, CertificateInfo certificateInfo) {
        this.id = id;
        this.certData = certificateInfo.getCertDataStr();
        this.privateKey = certificateInfo.getPrivateKeyStr();
        this.publicKey = certificateInfo.getPublicKeyStr();
    }
}
