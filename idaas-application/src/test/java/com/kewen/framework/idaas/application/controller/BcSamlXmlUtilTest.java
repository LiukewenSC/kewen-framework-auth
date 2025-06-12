package com.kewen.framework.idaas.application.controller;

import com.kewen.framework.idaas.application.model.certificate.CertificateGen;
import com.kewen.framework.idaas.application.util.BcCertificateUtil;
import org.junit.Test;

import java.util.Date;


public class BcSamlXmlUtilTest {

    @Test
    public void generateTest() {
        CertificateGen certificateGen = new CertificateGen();
        certificateGen.setSubject("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setIssuer("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24))
                .setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)))
        ;

        BcCertificateUtil.generate(certificateGen);
    }
}