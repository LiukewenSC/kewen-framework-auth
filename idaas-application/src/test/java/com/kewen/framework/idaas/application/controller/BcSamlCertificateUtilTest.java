package com.kewen.framework.idaas.application.controller;

import com.kewen.framework.idaas.application.saml.util.BcCertificateUtil;
import org.junit.Test;

import java.util.Date;


public class BcSamlCertificateUtilTest {

    @Test
    public void generateTest() {
        BcCertificateUtil.CertificateReq certificateReq = new BcCertificateUtil.CertificateReq();
        certificateReq.setSubject("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setIssuer("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24))
                .setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)))
        ;

        BcCertificateUtil.generate(certificateReq);
    }
}