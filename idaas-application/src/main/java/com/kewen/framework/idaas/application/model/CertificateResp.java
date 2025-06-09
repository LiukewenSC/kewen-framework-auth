package com.kewen.framework.idaas.application.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CertificateResp {
    private Long id;
    private String certData;
    private String privateKey;
    private String publicKey;
}
