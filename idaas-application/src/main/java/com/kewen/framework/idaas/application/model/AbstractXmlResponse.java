package com.kewen.framework.idaas.application.model;

import com.kewen.framework.idaas.application.config.SPCredentials;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.util.SamlXmlUtil;
import org.apache.xml.security.utils.EncryptionConstants;
import org.joda.time.DateTime;
import org.opensaml.saml.common.SAMLVersion;
import org.opensaml.saml.saml2.core.*;
import org.opensaml.saml.saml2.core.impl.*;
import org.opensaml.saml.saml2.encryption.Encrypter;
import org.opensaml.xmlsec.encryption.support.DataEncryptionParameters;
import org.opensaml.xmlsec.encryption.support.EncryptionException;
import org.opensaml.xmlsec.encryption.support.KeyEncryptionParameters;

import java.util.List;

/**
 * 抽象的返回对象
 *
 * @author kewen
 * @since 2025-06-10
 */
public abstract class AbstractXmlResponse {


    /**
     * 实体ID
     *
     * @return
     */
    public abstract String getEntityId();

    /**
     * 目标地址
     *
     * @return
     */
    public abstract String getDestination();


    /**
     * 证书内容
     *
     * @return
     */
    protected abstract String getCertData();

    protected abstract CertificateInfo getCertificateInfo();


    /**
     * 签发有效期
     *
     * @return
     */
    protected DateTime getIssueInstant() {
        return notAfter();
    }

    /**
     * 截止时间
     *
     * @return
     */
    protected abstract DateTime notAfter();

    /**
     * 生效时间
     *
     * @return
     */
    protected DateTime notBefore() {
        return new DateTime().minusMinutes(2);
    }

    /**
     * @return
     */
    protected abstract String getAudienceURI();

    /**
     * 是否获取签名报文，一般不需要
     *
     * @return
     */
    public boolean isEncryptResponse() {
        return false;
    }

    /**
     * 获取签名XML报文
     *
     * @return
     */
    public ArtifactResponse getArtifactResponse() {

        ArtifactResponse artifactResponse = new ArtifactResponseBuilder().buildObject();

        Issuer issuer = getIssuer();
        artifactResponse.setIssuer(issuer);

        artifactResponse.setIssueInstant(new DateTime());
        artifactResponse.setDestination(getDestination());

        artifactResponse.setID(SamlXmlUtil.generateSecureRandomId());

        Status status = getStatus();
        artifactResponse.setStatus(status);

        Response response = getResponse();
        artifactResponse.setMessage(response);

        return artifactResponse;
    }


    /**
     * 获取ＸＭＬ报文
     *
     * @return
     */
    public Response getResponse() {
        Response response = new ResponseBuilder().buildObject();
        //response.setID("_34679165192bb618761a2a588325811d");
        response.setID(SamlXmlUtil.generateSecureRandomId());

        response.setIssuer(getIssuer());

        response.setIssueInstant(getIssueInstant());

        response.setDestination(getDestination());

        Status status = getStatus();
        response.setStatus(status);

        Assertion assertion = getAssertion();
        CertificateInfo certificateInfo = getCertificateInfo();
        SamlXmlUtil.signAssertion(assertion, certificateInfo);

        response.getAssertions().add(assertion);

        //这里是加密的，可以不要，根据协议要求来
        EncryptedAssertion encryptedAssertion = encryptAssertion(assertion);
        response.getEncryptedAssertions().add(encryptedAssertion);

        return response;
    }


    public Status getStatus() {
        Status status = new StatusBuilder().buildObject();
        StatusCode statusCode = new StatusCodeBuilder().buildObject();
        statusCode.setValue(StatusCode.SUCCESS);
        status.setStatusCode(statusCode);
        return status;
    }

    public Issuer getIssuer() {
        Issuer issuer = new IssuerBuilder().buildObject();
        issuer.setValue(getEntityId());
        return issuer;
    }

    public Assertion getAssertion() {
        Assertion assertion = new AssertionBuilder().buildObject();
        assertion.setIssuer(getIssuer());
        assertion.setIssueInstant(new DateTime());
        assertion.setID(SamlXmlUtil.generateSecureRandomId());
        assertion.setVersion(SAMLVersion.VERSION_20);

        assertion.setSubject(getSubject());

        assertion.setConditions(getConditions());


        assertion.getAttributeStatements().add(getAttributeStatement());

        assertion.getAuthnStatements().add(buildAuthnStatement());
        return assertion;
    }

    /**
     * 加密断言
     */
    public EncryptedAssertion encryptAssertion(Assertion assertion) {
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

    public AttributeStatement getAttributeStatement() {
        AttributeStatement attributeStatement = new AttributeStatementBuilder().buildObject();
        attributeStatement.getAttributes().addAll(getAttributes());
        return attributeStatement;
    }

    public abstract List<Attribute> getAttributes();


    private AuthnStatement buildAuthnStatement() {
        AuthnStatement authnStatement = new AuthnStatementBuilder().buildObject();
        AuthnContext authnContext = new AuthnContextBuilder().buildObject();
        AuthnContextClassRef authnContextClassRef = new AuthnContextClassRefBuilder().buildObject();
        authnContextClassRef.setAuthnContextClassRef(AuthnContext.SMARTCARD_AUTHN_CTX);
        authnContext.setAuthnContextClassRef(authnContextClassRef);
        authnStatement.setAuthnContext(authnContext);

        authnStatement.setAuthnInstant(new DateTime());

        return authnStatement;
    }

    public Subject getSubject() {
        Subject subject = new SubjectBuilder().buildObject();
        NameID nameID = new NameIDBuilder().buildObject();
        nameID.setValue("IDP4admin");
        nameID.setFormat(NameIDType.TRANSIENT);
        nameID.setSPNameQualifier("SP name qualifier");
        nameID.setNameQualifier("Name qualifier");
        subject.setNameID(nameID);
        SubjectConfirmation subjectConfirmation = getSubjectConfirmation();
        subject.getSubjectConfirmations().add(subjectConfirmation);
        return subject;
    }

    private SubjectConfirmation getSubjectConfirmation() {
        SubjectConfirmation subjectConfirmation = new SubjectConfirmationBuilder().buildObject();
        //可选可不选
        subjectConfirmation.setMethod(SubjectConfirmation.METHOD_BEARER);
        SubjectConfirmationData subjectConfirmationData = new SubjectConfirmationDataBuilder().buildObject();
        //subjectConfirmationData.setNotBefore(notBefore());
        subjectConfirmationData.setNotOnOrAfter(notAfter());
        //todo InResponseTo  是 request的唯一ID
        //subjectConfirmationData.setInResponseTo();

        subjectConfirmationData.setRecipient(getDestination());
        subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);
        return subjectConfirmation;
    }

    public Conditions getConditions() {
        Conditions conditions = new ConditionsBuilder().buildObject();

        conditions.setNotBefore(notBefore());

        conditions.setNotOnOrAfter(notAfter());

        AudienceRestriction audienceRestriction = new AudienceRestrictionBuilder().buildObject();
        audienceRestriction.getAudiences().add(getAudience());
        conditions.getAudienceRestrictions().add(audienceRestriction);
        return conditions;
    }

    protected Audience getAudience() {
        Audience audience = new AudienceBuilder().buildObject();
        audience.setAudienceURI(getAudienceURI());
        return audience;
    }
}