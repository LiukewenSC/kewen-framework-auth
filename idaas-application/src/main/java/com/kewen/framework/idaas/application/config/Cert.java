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

package com.kewen.framework.idaas.application.config;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.io.*;
import java.security.Principal;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * 2025/06/11
 *
 * @author kewen
 * @since 4.23.0-mysql-aliyun-sovereign
 */
public class Cert implements Serializable {
    // get log4j handler

    private static final long serialVersionUID = 20130726;
    public static final String X509_TYPE = "X.509";
    public static final String SUN_X509_TYPE = "SunX509";
    public static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERTIFICATE = "-----END CERTIFICATE-----";
    public static final MediaType X509_MIME_TYPE = new MediaType("application",
            "x-x509-ca-cert");

    protected String alias;
    protected X509Certificate[] x509Certificates;

    public Cert(Cert copy) {
        alias = copy.getAlias();
        x509Certificates = copy.getChain() != null ? copy.getChain().clone()
                : null;
    }

    public Cert(X509Certificate x509) {
        this(null, x509);
    }

    protected Cert(X509Certificate[] certificates) {
        this(null, certificates);
    }

    public Cert(String alias, X509Certificate x509Certificate) {
        this(alias, new X509Certificate[]{x509Certificate});
    }

    public Cert(String alias, X509Certificate[] certificates) {
        this.alias = alias;
        this.x509Certificates = certificates;
    }

    public static Cert importCert(byte[] bytes, boolean checkValidity)
            throws CertificateException, IOException {
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            return importCert(inputStream, checkValidity);
        }
    }

    public static Cert importCert(byte[] bytes) throws CertificateException,
            IOException {
        return importCert(bytes, true);
    }

    public static Cert importCert(String pemCert) throws CertificateException,
            IOException {
        try (InputStream inputStream = new ByteArrayInputStream(
                pemCert.getBytes())) {
            return importCert(inputStream, true);
        }
    }

    public static Cert importCert(InputStream certStream)
            throws CertificateException {
        return importCert(certStream, true);
    }

    public static Cert importCert(InputStream certStream, boolean checkValidity)
            throws CertificateException {

        CertificateFactory certFactory = CertificateFactory
                .getInstance(X509_TYPE);
        Collection<? extends Certificate> collection = certFactory
                .generateCertificates(certStream);

//		if (collection == null || collection.size() == 0) {
//			logger.debug("CER-002 Failed to decode certificate from store: "
//					+ collection.size());
//		} else {
//			logger.debug("CER-003 Found certificate in Collection: "
//					+ collection.size());
//		}

        List<X509Certificate> certs = new ArrayList<X509Certificate>();
        for (Certificate c : collection) {
            certs.add((X509Certificate) c);
        }
        if (checkValidity) {
            if (certs.size() > 0) {
                certs.get(0).checkValidity();
            } else {
                throw new CertificateException(
                        "Could not find a valid certificate.");
            }
        }
        return new Cert(certs.get(0));
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public X509Certificate getX509Certificate() {
        return x509Certificates[0];
    }

    public X509Certificate[] getChain() {
        return x509Certificates;
    }

    public byte[] getEncoded() {

        try {
            return getX509Certificate().getEncoded();
        } catch (CertificateEncodingException e) {
            return new byte[0];
        }
    }

    /**
     * Export the certificate in PEM format.
     *
     * @return certificate in PEM format with the "-----BEGIN CERTIFICATE-----"
     *         prefix and "-----END CERTIFICATE-----" suffix
     * @throws CertificateEncodingException
     */
    public String exportCert() throws CertificateEncodingException {
        StringWriter buff = new StringWriter();
        PrintWriter out = new PrintWriter(buff);
        byte[] derCert = getX509Certificate().getEncoded();
        String pemCert = StringUtils.newStringUtf8(Base64
                .encodeBase64Chunked(derCert));
        out.println(BEGIN_CERTIFICATE);
        out.println(pemCert);
        out.println(END_CERTIFICATE);

        out.close();

        return buff.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cert)
                || !this.getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        return getX509Certificate().equals(((Cert) obj).getX509Certificate());
    }

    public int hashCode() {
        return getX509Certificate().hashCode();
    }

    public static Comparator<Cert> COMPARE_BY_SERIAL = new Comparator<Cert>() {
        public int compare(Cert cert1, Cert cert2) {
            return cert1.getX509Certificate().getSerialNumber()
                    .compareTo(cert2.getX509Certificate().getSerialNumber());
        }
    };

    public static Comparator<Cert> COMPARE_BY_SUBJECT_DN = new Comparator<Cert>() {
        public int compare(Cert cert1, Cert cert2) {
            Principal subjectDN1 = cert1.getX509Certificate().getSubjectDN();
            Principal subjectDN2 = cert2.getX509Certificate().getSubjectDN();
            return subjectDN1.toString().compareToIgnoreCase(
                    subjectDN2.toString());
        }
    };

    public static Comparator<Cert> COMPARE_BY_EXPIRATION_DATE = new Comparator<Cert>() {
        public int compare(Cert cert1, Cert cert2) {
            return cert1.getX509Certificate().getNotAfter()
                    .compareTo(cert2.getX509Certificate().getNotAfter());
        }
    };

    public static Comparator<Cert> COMPARE_BY_ALIAS = new Comparator<Cert>() {
        public int compare(Cert cert1, Cert cert2) {
            if (cert1.alias != null) {
                return cert1.alias.compareTo(cert2.alias);
            }

            return (cert2.alias != null) ? -1 : 0;
        }
    };
}
