package com.kewen.framework.idaas.application.model.certificate;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CertificateGen {
    protected String subject;
    protected String issuer;
    protected Date notBefore;
    protected Date notAfter;
    protected String signatureAlgorithm = "SHA256withRSA";

}
