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

package com.kewen.framework.idaas.application.controller;

import com.kewen.framework.idaas.application.config.IDPConstants;
import com.kewen.framework.idaas.application.config.IDPCredentials;
import com.kewen.framework.idaas.application.saml.util.OpenSAMLUtils;
import com.kewen.framework.idaas.application.saml.util.ResponseUtil;
import com.kewen.framework.idaas.application.saml.util.SPConstants;
import com.kewen.framework.idaas.application.saml.util.SPCredentials;
import org.apache.xml.security.utils.EncryptionConstants;
import org.joda.time.DateTime;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.core.xml.schema.XSString;
import org.opensaml.core.xml.schema.impl.XSAnyBuilder;
import org.opensaml.core.xml.schema.impl.XSStringBuilder;
import org.opensaml.saml.common.SAMLVersion;
import org.opensaml.saml.saml2.binding.decoding.impl.HTTPSOAP11Decoder;
import org.opensaml.saml.saml2.core.ArtifactResponse;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.AttributeValue;
import org.opensaml.saml.saml2.core.Audience;
import org.opensaml.saml.saml2.core.AudienceRestriction;
import org.opensaml.saml.saml2.core.AuthnContext;
import org.opensaml.saml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml.saml2.core.AuthnStatement;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.EncryptedAssertion;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.NameIDType;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.Status;
import org.opensaml.saml.saml2.core.StatusCode;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.opensaml.saml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.saml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml.saml2.core.impl.ResponseBuilder;
import org.opensaml.saml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml.saml2.core.impl.SubjectBuilder;
import org.opensaml.saml.saml2.core.impl.SubjectConfirmationBuilder;
import org.opensaml.saml.saml2.core.impl.SubjectConfirmationDataBuilder;
import org.opensaml.saml.saml2.encryption.Encrypter;
import org.opensaml.xmlsec.encryption.support.DataEncryptionParameters;
import org.opensaml.xmlsec.encryption.support.EncryptionException;
import org.opensaml.xmlsec.encryption.support.KeyEncryptionParameters;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.X509Certificate;
import org.opensaml.xmlsec.signature.X509Data;
import org.opensaml.xmlsec.signature.impl.KeyInfoBuilder;
import org.opensaml.xmlsec.signature.impl.SignatureBuilder;
import org.opensaml.xmlsec.signature.impl.SignatureImpl;
import org.opensaml.xmlsec.signature.impl.X509CertificateBuilder;
import org.opensaml.xmlsec.signature.impl.X509DataBuilder;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.opensaml.xmlsec.signature.support.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 2025/05/13
 *<?xml version="1.0" encoding="UTF-8"?>
 * <saml2p:Response Destination="https://signin.aliyun.com/saml-role/sso" ID="_34679165192bb618761a2a588325811d" IssueInstant="2025-01-26T09:47:55.868Z" Version="2.0" xmlns:saml2p="urn:oasis:names:tc:SAML:2.0:protocol">
 *     <saml2:Issuer xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion">kewen-idp-aliyun</saml2:Issuer>
 *     <saml2p:Status>
 *         <saml2p:StatusCode Value="urn:oasis:names:tc:SAML:2.0:status:Success"/>
 *     </saml2p:Status>
 *     <saml2:Assertion ID="_894ac19e4a9eda2d283059e0f1821889" IssueInstant="2025-01-26T09:47:55.853Z" Version="2.0" xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion">
 *         <saml2:Issuer>kewen-idp-aliyun</saml2:Issuer>
 *         <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
 *             <ds:SignedInfo>
 *                 <ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
 *                 <ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/>
 *                 <ds:Reference URI="#_894ac19e4a9eda2d283059e0f1821889">
 *                     <ds:Transforms>
 *                         <ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
 *                         <ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
 *                     </ds:Transforms>
 *                     <ds:DigestMethod Algorithm="http://www.w3.org/2001/04/xmlenc#sha256"/>
 *                     <ds:DigestValue>5Lp5pWqoBt8I4x5/oyP1diJ3abt7cFYIhUScsyqDQdE=</ds:DigestValue>
 *                 </ds:Reference>
 *             </ds:SignedInfo>
 *             <ds:SignatureValue>
 *                 PujyMLWQRrJVNRlNFbQ9sw5DFj0et3HmZURL8n3YKeRkKMdFvEAZZwz4y+SkMDq1UnjOamuoUQOK&#xd;
 *                 vgpLleGBLfDCEMNjbGCJaNUofX4uId7VMVJA5TEx4pg7WlbbUqT4NEZQVU9o0b/mKuZkMbf38oOm&#xd;
 *                 POmPU5LlYKvx7Obtl9Q=
 *             </ds:SignatureValue>
 *             <ds:KeyInfo>
 *                 <ds:X509Data>
 *                     <ds:X509Certificate>MIICITCCAYqgAwIBAgIIMxGAW8xkJCUwDQYJKoZIhvcNAQEFBQAwUjELMAkGA1UEBhMCQ04xDzAN
 *                         BgNVBAgMBuWbm+W3nTEPMA0GA1UEBwwG5oiQ6YO9MSEwHwYDVQQDDBjmtYvor5XpmL/ph4zkupHo
 *                         p5LoibJTU08wIBcNMjUwMTI2MDkyOTUyWhgPMzAyMzA1MzAwOTI5NTJaMFIxCzAJBgNVBAYTAkNO
 *                         MQ8wDQYDVQQIDAblm5vlt50xDzANBgNVBAcMBuaIkOmDvTEhMB8GA1UEAwwY5rWL6K+V6Zi/6YeM
 *                         5LqR6KeS6ImyU1NPMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYwmVynHrlYCqL3Y6I1ckJ
 *                         7rWzQL43KOKG2G0pSdY3XnXKLcGfJhc446fRGSfQC511cylGy2oOYbDHyXbavECtieekVHwIn+Sc
 *                         By1Quyj07+33JjkGeCw7EA2BtsErZNryk2AAS3kf0B75PgtZutK8wo3Mhse+4uMa13cZxas8HQID
 *                         AQABMA0GCSqGSIb3DQEBBQUAA4GBAGdpD24an1QmWrecAEZei1Y76zpGcG5wG+6i/gVjGXQ2oMmJ
 *                         uMkSTVZPnjyGz5yOIVjdMeQB71BNtVkJ1caqw1Cru+CrQWa/y9NM9XAyFfqHvznimCu2AneYmNo7
 *                         ntpoGNpvmTrxt6gyPZO2ijPwau/ex10MLmbA3IWNtNDng4wI</ds:X509Certificate>
 *                 </ds:X509Data>
 *             </ds:KeyInfo>
 *         </ds:Signature>
 *         <saml2:Subject>
 *             <saml2:NameID Format="urn:oasis:names:tc:SAML:2.0:nameid-format:persistent">IDP4admin</saml2:NameID>
 *             <saml2:SubjectConfirmation Method="urn:oasis:names:tc:SAML:2.0:cm:bearer">
 *                 <saml2:SubjectConfirmationData NotOnOrAfter="2025-01-26T10:47:55.853Z" Recipient="https://signin.aliyun.com/saml-role/sso"/>
 *             </saml2:SubjectConfirmation>
 *         </saml2:Subject>
 *         <saml2:Conditions NotBefore="2025-01-26T09:42:55.853Z" NotOnOrAfter="2025-01-26T10:47:55.853Z">
 *             <saml2:AudienceRestriction>
 *                 <saml2:Audience>urn:alibaba:cloudcomputing</saml2:Audience>
 *             </saml2:AudienceRestriction>
 *         </saml2:Conditions>
 *         <saml2:AttributeStatement>
 *             <saml2:Attribute Name="https://www.aliyun.com/SAML-Role/Attributes/Role" NameFormat="urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified">
 *                 <saml2:AttributeValue>acs:ram::1555734646214700:role/old-idp-role,acs:ram::1555734646214700:saml-provider/old-idp</saml2:AttributeValue>
 *             </saml2:Attribute>
 *             <saml2:Attribute Name="https://www.aliyun.com/SAML-Role/Attributes/RoleSessionName" NameFormat="urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified">
 *                 <saml2:AttributeValue>IDP4admin</saml2:AttributeValue>
 *             </saml2:Attribute>
 *             <saml2:Attribute Name="https://www.aliyun.com/SAML-Role/Attributes/SessionDuration" NameFormat="urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified">
 *                 <saml2:AttributeValue>3600</saml2:AttributeValue>
 *             </saml2:Attribute>
 *         </saml2:AttributeStatement>
 *         <saml2:AuthnStatement AuthnInstant="2025-01-26T09:47:55.853Z" SessionIndex="_b0026c7b51a0bba19924f818a39b2f7b" SessionNotOnOrAfter="2025-01-26T10:47:55.853Z">
 *             <saml2:AuthnContext>
 *                 <saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Password</saml2:AuthnContextClassRef>
 *             </saml2:AuthnContext>
 *         </saml2:AuthnStatement>
 *     </saml2:Assertion>
 * </saml2p:Response>
 * @author kewen
 * @since 4.23.0-mysql-aliyun-sovereign
 */
@RequestMapping("/sso")
@Controller
public class AliyunRoleSsoController {

    private static final Logger log = LoggerFactory.getLogger(AliyunRoleSsoController.class);

    @GetMapping("/go")
    @ResponseBody
    public void goSso(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        // 跳转到阿里云角色sso
        Response response = new ResponseBuilder().buildObject();
        response.setID("_34679165192bb618761a2a588325811d");

        Issuer issuer = new IssuerBuilder().buildObject();
        issuer.setValue("kewen-idp-aliyun");
        response.setIssuer(issuer);

        DateTime issueInstant = new DateTime().plusDays(1);
        response.setIssueInstant(issueInstant);

        Status status = new StatusBuilder().buildObject();
        StatusCode statusCode = new StatusCodeBuilder().buildObject();
        statusCode.setValue("urn:oasis:names:tc:SAML:2.0:status:Success");
        status.setStatusCode(statusCode);
        response.setStatus(status);

        Assertion assertion = getAssertion();
        String certData = getCertData();
        signAssertion(assertion, certData);

        response.getAssertions().add(assertion);
        response.setDestination("https://signin.aliyun.com/saml-role/sso");


        ResponseUtil.redirect(httpServletResponse, response, "https://signin.aliyun.com/saml-role/sso");

    }

    private Assertion getAssertion() {
        Assertion assertion = new AssertionBuilder().buildObject();
        //assertion.setIssuer(issuer);
        //assertion.setIssueInstant(issueInstant);
        assertion.setID("_894ac19e4a9eda2d283059e0f1821889");
        assertion.setVersion(SAMLVersion.VERSION_20);

        assertion.setSubject(getSubject());

        assertion.setConditions(getConditions());


        assertion.getAttributeStatements().add(getAttributeStatement());
        return assertion;
    }

    private static AttributeStatement getAttributeStatement() {
        AttributeStatement attributeStatement = new AttributeStatementBuilder().buildObject();
        Attribute attribute = new AttributeBuilder().buildObject();
        // attribute value 没有Builder
        //AttributeValue attributeValue = null;
        XSAny attrValue = new XSAnyBuilder().buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attrValue.setTextContent("acs:ram::1555734646214700:role/old-idp-role,acs:ram::1555734646214700:saml-provider/old-idp");
        attribute.getAttributeValues().add(attrValue);
        attributeStatement.getAttributes().add(attribute);
        return attributeStatement;
    }

    private static Subject getSubject() {
        Subject subject = new SubjectBuilder().buildObject();
        NameID nameID = new NameIDBuilder().buildObject();
        nameID.setValue("IDP4admin");
        nameID.setFormat(NameIDType.TRANSIENT);
        nameID.setSPNameQualifier("SP name qualifier");
        nameID.setNameQualifier("Name qualifier");
        subject.setNameID(nameID);
        SubjectConfirmation subjectConfirmation = new SubjectConfirmationBuilder().buildObject();
        SubjectConfirmationData subjectConfirmationData = new SubjectConfirmationDataBuilder().buildObject();
        subjectConfirmationData.setNotBefore(new DateTime().plusDays(-1));
        subjectConfirmationData.setNotOnOrAfter(new DateTime().plusDays(1));
        subjectConfirmationData.setRecipient("https://signin.aliyun.com/saml-role/sso");
        subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);
        subject.getSubjectConfirmations().add(subjectConfirmation);
        return subject;
    }

    private static Conditions getConditions() {
        Conditions conditions = new ConditionsBuilder().buildObject();
        AudienceRestriction audienceRestriction = new AudienceRestrictionBuilder().buildObject();
        Audience audience = new AudienceBuilder().buildObject();
        audience.setAudienceURI("urn:alibaba:cloudcomputing");
        audienceRestriction.getAudiences().add(audience);
        conditions.getAudienceRestrictions().add(audienceRestriction);
        return conditions;
    }

    private static SignatureImpl getSignature() {
        SignatureImpl signature = new SignatureBuilder().buildObject();
        signature.setSigningCredential(IDPCredentials.getCredential());
        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        return signature;
    }

    @PostMapping("/go2")
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        log.debug("recieved artifactResolve:");
        HTTPSOAP11Decoder decoder = new HTTPSOAP11Decoder();


        /*decoder.setHttpServletRequest(req);

        try {
            BasicParserPool parserPool = new BasicParserPool();
            parserPool.initialize();
            decoder.setParserPool(parserPool);
            decoder.initialize();
            decoder.decode();
        } catch (MessageDecodingException e) {
            throw new RuntimeException(e);
        } catch (ComponentInitializationException e) {
            throw new RuntimeException(e);
        }

        OpenSAMLUtils.logSAMLObject(decoder.getMessageContext().getMessage());*/

        ArtifactResponse artifactResponse = buildArtifactResponse();

        ResponseUtil.redirect(resp, artifactResponse, "https://signin.aliyun.com/saml-role/sso");

    }


    private ArtifactResponse buildArtifactResponse() {

        ArtifactResponse artifactResponse = OpenSAMLUtils.buildSAMLObject(ArtifactResponse.class);

        Issuer issuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
        issuer.setValue(IDPConstants.IDP_ENTITY_ID);
        artifactResponse.setIssuer(issuer);
        artifactResponse.setIssueInstant(new DateTime());
        artifactResponse.setDestination(SPConstants.ASSERTION_CONSUMER_SERVICE);

        artifactResponse.setID(OpenSAMLUtils.generateSecureRandomId());

        Status status = OpenSAMLUtils.buildSAMLObject(Status.class);
        StatusCode statusCode = OpenSAMLUtils.buildSAMLObject(StatusCode.class);
        statusCode.setValue(StatusCode.SUCCESS);
        status.setStatusCode(statusCode);
        artifactResponse.setStatus(status);

        Response response = OpenSAMLUtils.buildSAMLObject(Response.class);
        response.setDestination(SPConstants.ASSERTION_CONSUMER_SERVICE);
        response.setIssueInstant(new DateTime());
        response.setID(OpenSAMLUtils.generateSecureRandomId());
        Issuer issuer2 = OpenSAMLUtils.buildSAMLObject(Issuer.class);
        issuer2.setValue(IDPConstants.IDP_ENTITY_ID);

        response.setIssuer(issuer2);

        Status status2 = OpenSAMLUtils.buildSAMLObject(Status.class);
        StatusCode statusCode2 = OpenSAMLUtils.buildSAMLObject(StatusCode.class);
        statusCode2.setValue(StatusCode.SUCCESS);
        status2.setStatusCode(statusCode2);

        response.setStatus(status2);

        artifactResponse.setMessage(response);

        Assertion assertion = buildAssertion();

        signAssertion(assertion, null);
        EncryptedAssertion encryptedAssertion = encryptAssertion(assertion);

        response.getEncryptedAssertions().add(encryptedAssertion);
        return artifactResponse;
    }

    /**
     * 加密断言
     */
    private EncryptedAssertion encryptAssertion(Assertion assertion) {
        DataEncryptionParameters encryptionParameters = new DataEncryptionParameters();
        encryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);

        KeyEncryptionParameters keyEncryptionParameters = new KeyEncryptionParameters();
        keyEncryptionParameters.setEncryptionCredential(SPCredentials.getCredential());
        keyEncryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP);

        Encrypter encrypter = new Encrypter(encryptionParameters, keyEncryptionParameters);
        encrypter.setKeyPlacement(Encrypter.KeyPlacement.INLINE);

        try {
            return encrypter.encrypt(assertion);
        } catch (EncryptionException e) {
            throw new RuntimeException(e);
        }
    }

    private void signAssertion(Assertion assertion, String x509CertificateValue) {
        Signature signature = OpenSAMLUtils.buildSAMLObject(Signature.class);
        //todo 这里去掉默认的
        signature.setSigningCredential(IDPCredentials.getCredential());
        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        KeyInfo keyInfo = getKeyInfo(x509CertificateValue);
        signature.setKeyInfo(keyInfo);
        assertion.setSignature(signature);

        try {
            //noinspection ConstantConditions =》marshall 要求输入Nonnull且输出为Nonull；
            XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(assertion).marshall(assertion);
        } catch (MarshallingException e) {
            throw new RuntimeException(e);
        }

        try {
            Signer.signObject(signature);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    private KeyInfo getKeyInfo(String x509CertificateValue) {
        if (x509CertificateValue == null) {
            return null;
        }
        /*final X509KeyInfoGeneratorFactory x509KeyInfoGeneratorFactory = new X509KeyInfoGeneratorFactory();
        x509KeyInfoGeneratorFactory.setEmitEntityCertificate(true);
        final KeyInfoGenerator keyInfoGenerator = x509KeyInfoGeneratorFactory.newInstance();
        KeyInfo keyInfo = keyInfoGenerator.generate(credential);*/

        KeyInfo keyInfo = new KeyInfoBuilder().buildObject();
        X509Data x509Data = new X509DataBuilder().buildObject();
        X509Certificate x509Certificate = new X509CertificateBuilder().buildObject();
        x509Certificate.setValue(x509CertificateValue);
        x509Data.getX509Certificates().add(x509Certificate);
        keyInfo.getX509Datas().add(x509Data);
        return keyInfo;
    }

    private String getCertData() {
        CertificateGenerator.CertificateReq certificateReq = new CertificateGenerator.CertificateReq();
        certificateReq.setSubject("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setIssuer("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24))
                .setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)))
        ;
        CertificateGenerator.CertificateResp generate = CertificateGenerator.generate(certificateReq);
        return generate.getCertData();
    }

    private Assertion buildAssertion() {

        Assertion assertion = OpenSAMLUtils.buildSAMLObject(Assertion.class);

        Issuer issuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
        issuer.setValue(IDPConstants.IDP_ENTITY_ID);
        assertion.setIssuer(issuer);
        assertion.setIssueInstant(new DateTime());

        assertion.setID(OpenSAMLUtils.generateSecureRandomId());


        Subject subject = buildSubject();

        assertion.setSubject(subject);

        assertion.setConditions(getConditions());

        assertion.getAttributeStatements().add(buildAttributeStatement());

        assertion.getAuthnStatements().add(buildAuthnStatement());

        return assertion;
    }

    private Subject buildSubject() {
        NameID nameID = OpenSAMLUtils.buildSAMLObject(NameID.class);
        nameID.setFormat(NameIDType.TRANSIENT);
        nameID.setValue("Some NameID value");
        nameID.setSPNameQualifier("SP name qualifier");
        nameID.setNameQualifier("Name qualifier");
        Subject subject = OpenSAMLUtils.buildSAMLObject(Subject.class);
        subject.setNameID(nameID);
        subject.getSubjectConfirmations().add(buildSubjectConfirmation());
        return subject;
    }

    private SubjectConfirmation buildSubjectConfirmation() {
        SubjectConfirmation subjectConfirmation = OpenSAMLUtils.buildSAMLObject(SubjectConfirmation.class);
        subjectConfirmation.setMethod(SubjectConfirmation.METHOD_BEARER);

        SubjectConfirmationData subjectConfirmationData = OpenSAMLUtils.buildSAMLObject(SubjectConfirmationData.class);
        subjectConfirmationData.setInResponseTo("Made up ID");
        subjectConfirmationData.setNotBefore(new DateTime().minusDays(2));
        subjectConfirmationData.setNotOnOrAfter(new DateTime().plusDays(2));
        subjectConfirmationData.setRecipient(SPConstants.ASSERTION_CONSUMER_SERVICE);

        subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);

        return subjectConfirmation;
    }

    private AuthnStatement buildAuthnStatement() {
        AuthnStatement authnStatement = OpenSAMLUtils.buildSAMLObject(AuthnStatement.class);
        AuthnContext authnContext = OpenSAMLUtils.buildSAMLObject(AuthnContext.class);
        AuthnContextClassRef authnContextClassRef = OpenSAMLUtils.buildSAMLObject(AuthnContextClassRef.class);
        authnContextClassRef.setAuthnContextClassRef(AuthnContext.SMARTCARD_AUTHN_CTX);
        authnContext.setAuthnContextClassRef(authnContextClassRef);
        authnStatement.setAuthnContext(authnContext);

        authnStatement.setAuthnInstant(new DateTime());

        return authnStatement;
    }

    private Conditions buildConditions() {
        Conditions conditions = OpenSAMLUtils.buildSAMLObject(Conditions.class);
        conditions.setNotBefore(new DateTime().minusDays(2));
        conditions.setNotOnOrAfter(new DateTime().plusDays(2));
        AudienceRestriction audienceRestriction = OpenSAMLUtils.buildSAMLObject(AudienceRestriction.class);
        Audience audience = OpenSAMLUtils.buildSAMLObject(Audience.class);
        audience.setAudienceURI(SPConstants.ASSERTION_CONSUMER_SERVICE);
        audienceRestriction.getAudiences().add(audience);
        conditions.getAudienceRestrictions().add(audienceRestriction);
        return conditions;
    }

    private AttributeStatement buildAttributeStatement() {
        AttributeStatement attributeStatement = OpenSAMLUtils.buildSAMLObject(AttributeStatement.class);

        Attribute attributeUserName = OpenSAMLUtils.buildSAMLObject(Attribute.class);

        XSStringBuilder stringBuilder = (XSStringBuilder)XMLObjectProviderRegistrySupport.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
        assert stringBuilder != null;
        XSString userNameValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
        userNameValue.setValue("bob");

        attributeUserName.getAttributeValues().add(userNameValue);
        attributeUserName.setName("username");
        attributeStatement.getAttributes().add(attributeUserName);

        Attribute attributeLevel = OpenSAMLUtils.buildSAMLObject(Attribute.class);
        XSString levelValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
        levelValue.setValue("999999999");

        attributeLevel.getAttributeValues().add(levelValue);
        attributeLevel.setName("telephone");
        attributeStatement.getAttributes().add(attributeLevel);

        return attributeStatement;

    }

}
