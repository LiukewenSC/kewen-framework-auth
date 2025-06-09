package com.kewen.framework.idaas.application.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CertificateReq {
    private String subject;
    private String issuer;
    private String serial;
    private Date notBefore;
    private Date notAfter;
    private String signatureAlgorithm = "SHA256withRSA";

}
