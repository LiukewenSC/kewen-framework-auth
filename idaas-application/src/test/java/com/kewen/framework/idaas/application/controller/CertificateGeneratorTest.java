package com.kewen.framework.idaas.application.controller;

import org.junit.Test;

import java.util.Date;


public class CertificateGeneratorTest {

    @Test
    public void generateTest() {
        CertificateGenerator.CertificateReq certificateReq = new CertificateGenerator.CertificateReq();
        certificateReq.setSubject("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setIssuer("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24))
                .setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)))
        ;

        CertificateGenerator.generate(certificateReq);
    }
}