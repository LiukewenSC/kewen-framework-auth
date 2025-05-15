package com.kewen.framework.idaas.application.saml.util;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.xml.security.utils.EncryptionConstants;
import org.joda.time.DateTime;
import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.core.xml.schema.impl.XSAnyBuilder;
import org.opensaml.saml.common.SAMLVersion;
import org.opensaml.saml.saml2.core.*;
import org.opensaml.saml.saml2.core.impl.*;
import org.opensaml.saml.saml2.encryption.Encrypter;
import org.opensaml.xmlsec.encryption.support.DataEncryptionParameters;
import org.opensaml.xmlsec.encryption.support.EncryptionException;
import org.opensaml.xmlsec.encryption.support.KeyEncryptionParameters;
import org.opensaml.xmlsec.signature.KeyInfo;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Date;

public class SamlXmlObjectUtil {

    public static ArtifactResponse getArtifactResponse(String entityId, String destination) {

        ArtifactResponse artifactResponse = new ArtifactResponseBuilder().buildObject();

        Issuer issuer = getIssuer(entityId);
        artifactResponse.setIssuer(issuer);

        artifactResponse.setIssueInstant(new DateTime());
        artifactResponse.setDestination(destination);

        artifactResponse.setID(SamlCertificateUtil.generateSecureRandomId());

        Status status = getStatus();
        artifactResponse.setStatus(status);

        Response response = getResponse(entityId, destination);
        artifactResponse.setMessage(response);

        return artifactResponse;
    }

    public static Response getResponse(String entityId, String destination) {
        Response response = new ResponseBuilder().buildObject();
        //response.setID("_34679165192bb618761a2a588325811d");
        response.setID(SamlCertificateUtil.generateSecureRandomId());

        Issuer issuer = new IssuerBuilder().buildObject();
        issuer.setValue(entityId);
        response.setIssuer(issuer);

        DateTime issueInstant = new DateTime().plusDays(1);
        response.setIssueInstant(issueInstant);

        response.setDestination(destination);

        Status status = getStatus();
        response.setStatus(status);

        Assertion assertion = getAssertion(destination);
        String certData = getCertData();
        KeyInfo keyInfo = SamlCertificateUtil.getKeyInfo(certData);
        SamlCertificateUtil.signAssertion(assertion, keyInfo);

        response.getAssertions().add(assertion);

        //这里是加密的，可以不要，根据协议要求来
        EncryptedAssertion encryptedAssertion = encryptAssertion(assertion);
        response.getEncryptedAssertions().add(encryptedAssertion);

        return response;
    }

    private static String getCertData() {
        BcCertificateUtil.CertificateReq certificateReq = new BcCertificateUtil.CertificateReq();
        certificateReq.setSubject("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setIssuer("CN=John Doe, OU=Engineering, O=MyCompany, C=US")
                .setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24))
                .setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)))
        ;
        Pair<KeyPair, X509Certificate> keyX509Certificate = BcCertificateUtil.generate(certificateReq);
        BcCertificateUtil.CertificateResp certificateResp = BcCertificateUtil.getCertificateResp(
                keyX509Certificate.getLeft(), keyX509Certificate.getRight()
        );
        return certificateResp.getCertData();
    }

    public static Status getStatus() {
        Status status = new StatusBuilder().buildObject();
        StatusCode statusCode = new StatusCodeBuilder().buildObject();
        statusCode.setValue(StatusCode.SUCCESS);
        status.setStatusCode(statusCode);
        return status;
    }

    public static Issuer getIssuer(String issuerValue) {
        Issuer issuer = new IssuerBuilder().buildObject();
        issuer.setValue(issuerValue);
        return issuer;
    }

    public static Assertion getAssertion(String recipient) {
        Assertion assertion = new AssertionBuilder().buildObject();
        assertion.setIssuer(getIssuer("kewen"));
        assertion.setIssueInstant(new DateTime());
        assertion.setID(SamlCertificateUtil.generateSecureRandomId());
        assertion.setID(SamlCertificateUtil.generateSecureRandomId());
        assertion.setVersion(SAMLVersion.VERSION_20);

        assertion.setSubject(getSubject(recipient));

        assertion.setConditions(getConditions());


        assertion.getAttributeStatements().add(getAttributeStatement());

        assertion.getAuthnStatements().add(buildAuthnStatement());
        return assertion;
    }

    /**
     * 加密断言
     */
    public static EncryptedAssertion encryptAssertion(Assertion assertion) {
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

    public static AttributeStatement getAttributeStatement() {
        AttributeStatement attributeStatement = new AttributeStatementBuilder().buildObject();

        Attribute attribute = new AttributeBuilder().buildObject();
        //AttributeValue attributeValue = null; XSAny  XSString
        XSAny attrValue = new XSAnyBuilder().buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attrValue.setTextContent("acs:ram::1555734646214700:role/old-idp-role,acs:ram::1555734646214700:saml-provider/old-idp");
        attribute.getAttributeValues().add(attrValue);

        attributeStatement.getAttributes().add(attribute);
        return attributeStatement;
    }

    private static AuthnStatement buildAuthnStatement() {
        AuthnStatement authnStatement = new AuthnStatementBuilder().buildObject();
        AuthnContext authnContext = new AuthnContextBuilder().buildObject();
        AuthnContextClassRef authnContextClassRef = new AuthnContextClassRefBuilder().buildObject();
        authnContextClassRef.setAuthnContextClassRef(AuthnContext.SMARTCARD_AUTHN_CTX);
        authnContext.setAuthnContextClassRef(authnContextClassRef);
        authnStatement.setAuthnContext(authnContext);

        authnStatement.setAuthnInstant(new DateTime());

        return authnStatement;
    }

    public static Subject getSubject(String recipient) {
        Subject subject = new SubjectBuilder().buildObject();
        NameID nameID = new NameIDBuilder().buildObject();
        nameID.setValue("IDP4admin");
        nameID.setFormat(NameIDType.TRANSIENT);
        nameID.setSPNameQualifier("SP name qualifier");
        nameID.setNameQualifier("Name qualifier");
        subject.setNameID(nameID);
        SubjectConfirmation subjectConfirmation = getSubjectConfirmation(recipient);
        subject.getSubjectConfirmations().add(subjectConfirmation);
        return subject;
    }

    private static SubjectConfirmation getSubjectConfirmation(String recipient) {
        SubjectConfirmation subjectConfirmation = new SubjectConfirmationBuilder().buildObject();
        //可选可不选
        subjectConfirmation.setMethod(SubjectConfirmation.METHOD_BEARER);
        SubjectConfirmationData subjectConfirmationData = new SubjectConfirmationDataBuilder().buildObject();
        subjectConfirmationData.setNotBefore(new DateTime().plusDays(-1));
        subjectConfirmationData.setNotOnOrAfter(new DateTime().plusDays(1));
        //todo InResponseTo  是 request的唯一ID
        //subjectConfirmationData.setInResponseTo();

        subjectConfirmationData.setRecipient(recipient);
        subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);
        return subjectConfirmation;
    }

    public static Conditions getConditions() {
        Conditions conditions = new ConditionsBuilder().buildObject();

        conditions.setNotBefore(new DateTime().minusDays(2));

        conditions.setNotOnOrAfter(new DateTime().plusDays(2));

        AudienceRestriction audienceRestriction = new AudienceRestrictionBuilder().buildObject();
        Audience audience = new AudienceBuilder().buildObject();
        audience.setAudienceURI("urn:alibaba:cloudcomputing");
        audienceRestriction.getAudiences().add(audience);
        conditions.getAudienceRestrictions().add(audienceRestriction);

        return conditions;
    }
}
