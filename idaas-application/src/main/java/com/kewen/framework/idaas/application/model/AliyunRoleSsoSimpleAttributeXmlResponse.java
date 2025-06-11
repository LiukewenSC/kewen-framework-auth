package com.kewen.framework.idaas.application.model;

import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import org.joda.time.DateTime;
import org.opensaml.saml.saml2.core.Audience;
import org.opensaml.saml.saml2.core.impl.AudienceBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云对象返回
 *
 * @author kewen
 * @since 2025-06-10
 */
public class AliyunRoleSsoSimpleAttributeXmlResponse extends AbstractSimpleAttributeXmlResponse {

    private final String entityId;
    private final String destination;
    private final String certData;
    private final CertificateResp certificateResp;
    private final DateTime notAfter;
    private final String role;

    public AliyunRoleSsoSimpleAttributeXmlResponse(String entityId, String destination, CertificateResp certificateResp, DateTime notAfter, String role) {
        this.entityId = entityId;
        this.destination = destination;
        this.certData = certificateResp.getCertData();
        this.certificateResp = certificateResp;
        this.notAfter = notAfter;
        this.role = role;
    }


    /**
     * <saml2:AttributeStatement>
     * <saml2:Attribute Name="https://www.aliyun.com/SAML-Role/Attributes/Role" NameFormat="urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified">
     * <saml2:AttributeValue>acs:ram::1555734646214700:role/old-idp-role,acs:ram::1555734646214700:saml-provider/old-idp</saml2:AttributeValue>
     * </saml2:Attribute>
     * <saml2:Attribute Name="https://www.aliyun.com/SAML-Role/Attributes/RoleSessionName" NameFormat="urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified">
     * <saml2:AttributeValue>IDP4admin</saml2:AttributeValue>
     * </saml2:Attribute>
     * <saml2:Attribute Name="https://www.aliyun.com/SAML-Role/Attributes/SessionDuration" NameFormat="urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified">
     * <saml2:AttributeValue>3600</saml2:AttributeValue>
     * </saml2:Attribute>
     * </saml2:AttributeStatement>
     *
     * @return
     */
    @Override
    protected Map<String, String> getAttributeValues() {
        Map<String, String> attributeValues = new HashMap<>();
        attributeValues.put("https://www.aliyun.com/SAML-Role/Attributes/SessionDuration", "3600");
        attributeValues.put("https://www.aliyun.com/SAML-Role/Attributes/RoleSessionName", "session_" + entityId);
        attributeValues.put("https://www.aliyun.com/SAML-Role/Attributes/Role", role);
        return attributeValues;
    }

    @Override
    protected DateTime getIssueInstant() {
        return DateTime.now();
    }

    @Override
    public String getEntityId() {
        return entityId;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    protected String getCertData() {
        return certData;
    }

    @Override
    protected CertificateInfo getCertificateInfo() {
        return certificateResp.parseCertificateInfo();
    }

    @Override
    protected DateTime notAfter() {
        return notAfter;
    }

    @Override
    protected Audience getAudience() {
        Audience audience = new AudienceBuilder().buildObject();
        //audience.setAudienceURI(getAudienceURI());
        audience.setAudienceURI("urn:alibaba:cloudcomputing");
        return audience;
    }
}
