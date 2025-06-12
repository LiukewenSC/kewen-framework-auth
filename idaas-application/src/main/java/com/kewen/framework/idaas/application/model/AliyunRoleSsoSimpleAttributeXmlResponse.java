package com.kewen.framework.idaas.application.model;

import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云对象返回
 * https://help.aliyun.com/zh/ram/user-guide/saml-response-for-role-based-sso?spm=a2c4g.11186623.help-menu-28625.d_2_4_3_0_4.db4130f3qnyLCN
 * @author kewen
 * @since 2025-06-10
 */
public class AliyunRoleSsoSimpleAttributeXmlResponse extends AbstractSimpleAttributeXmlResponse {
    private static final String SSO_URL = "https://signin.aliyun.com/saml-role/sso";

    /**
     * IDP 唯一标识
     */
    private final String entityId;
    private final String username;
    private final String certData;
    private final CertificateInfoStr certificateInfoStr;
    private final DateTime notAfter;
    private final String role;

    public AliyunRoleSsoSimpleAttributeXmlResponse(String entityId, CertificateInfoStr certificateInfoStr, DateTime notAfter, String role, String username) {
        this.username = username;
        this.entityId = entityId;
        this.certData = certificateInfoStr.getX509CertificateDerStr();
        this.certificateInfoStr = certificateInfoStr;
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
    public String getUsername() {
        return username;
    }

    @Override
    public String getDestination() {
        return SSO_URL;
    }

    @Override
    protected String getCertData() {
        return certData;
    }

    @Override
    protected CertificateInfo getCertificateInfo() {
        return certificateInfoStr.parseCertificateInfo();
    }

    @Override
    protected DateTime notAfter() {
        return notAfter;
    }

    /**
     * <Conditions>
     * <AudienceRestriction>
     * <Audience>urn:alibaba:cloudcomputing</Audience>
     * </AudienceRestriction>
     * </Conditions>
     *
     * @return
     */
    @Override
    protected String getAudienceURI() {
        return "urn:alibaba:cloudcomputing";
    }
}
