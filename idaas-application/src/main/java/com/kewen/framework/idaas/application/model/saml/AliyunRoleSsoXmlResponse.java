package com.kewen.framework.idaas.application.model.saml;

import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfoStr;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云对象返回
 * https://help.aliyun.com/zh/ram/user-guide/saml-response-for-role-based-sso?spm=a2c4g.11186623.help-menu-28625.d_2_4_3_0_4.db4130f3qnyLCN
 *
 * <saml2p:Response xmlns:saml2p="urn:oasis:names:tc:SAML:2.0:protocol" Destination="https://signin.aliyun.com/saml-role/sso" ID="_5226f24f545eab764f499b6830e9688e" IssueInstant="2025-06-12T14:10:56.128Z" Version="2.0">
 *     <saml2:Issuer xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion">kewen-idp</saml2:Issuer>
 *     <saml2p:Status>
 *         <saml2p:StatusCode Value="urn:oasis:names:tc:SAML:2.0:status:Success"/>
 *     </saml2p:Status>
 *     <saml2:Assertion xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion" ID="_66d47533cbbb81344295016227d31b1b" IssueInstant="2025-06-12T14:10:56.128Z" Version="2.0">
 *         <saml2:Issuer>kewen-idp</saml2:Issuer>
 *         <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
 *             <ds:SignedInfo>
 *                 <ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
 *                 <ds:SignatureMethod Algorithm="http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"/>
 *                 <ds:Reference URI="#_66d47533cbbb81344295016227d31b1b">
 *                     <ds:Transforms>
 *                         <ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
 *                         <ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
 *                     </ds:Transforms>
 *                     <ds:DigestMethod Algorithm="http://www.w3.org/2001/04/xmlenc#sha256"/>
 *                     <ds:DigestValue>WWTZdyWXnpZv8DyLGcGp2UfMrCXcOc/Okgp4xfIMtII=</ds:DigestValue>
 *                 </ds:Reference>
 *             </ds:SignedInfo>
 *             <ds:SignatureValue>
 *                 H3jvdfzzIahq44PrexK08u9kLgFmwKK+p0ThvvDf4118Yo2eKEzsTdJpiviCM3+aj0OHTsgU1+VA&#13;
 *                 XJ52s60QM2gx5Kpcym1t9yxV+w4YfX8DyUKnERAi7v5878LKJS2W3FeeIdUj4j1lUNPv5spQAl/j&#13;
 *                 w5Jl0DLsSgMtIOKVxWMm10tl5xvWkFUjDl8cw5MQTTkmLWq8uWCbZ3oprjWdWISGc+DQHtR5n+lY&#13;
 *                 WJUBrqFXrSposVH0ryRLQScXmJLyiKG5wfZuByeTV7lWC1KQLTWi6+sE5ULTnaSJHR5MVivF00wh&#13;
 *                 5hA4BG2gqRa67ZhYjHYmx7IvrTEsCsOUxbp9pg==
 *             </ds:SignatureValue>
 *             <ds:KeyInfo>
 *                 <ds:X509Data>
 *                     <ds:X509Certificate>MIIDEjCCAfqgAwIBAgIGAZdVZzFoMA0GCSqGSIb3DQEBCwUAMEoxETAPBgNVBAMMCEpvaG4gRG9l
 *                         MRQwEgYDVQQLDAtFbmdpbmVlcmluZzESMBAGA1UECgwJTXlDb21wYW55MQswCQYDVQQGEwJVUzAe
 *                         Fw0yNTA2MDkxNTU1MTRaFw0yNjA2MDkxNTU1MTRaMEoxETAPBgNVBAMMCEpvaG4gRG9lMRQwEgYD
 *                         VQQLDAtFbmdpbmVlcmluZzESMBAGA1UECgwJTXlDb21wYW55MQswCQYDVQQGEwJVUzCCASIwDQYJ
 *                         KoZIhvcNAQEBBQADggEPADCCAQoCggEBAI1EYFijS5y4UFB8+ji7IOK+LUC+HfqvF7fKv5FS5kaC
 *                         bg+Dl5HdR8O2DIxl3C948BfkYFOasCkuFk7Z0sQq7Hh+VyEgWjYrrGDDXXFFvMEilO5uE+RxfHGt
 *                         a6gVtqxYOSE2htoprN5STbL2ct3kP/H4fp1xTqdWajA4lh5fDv/sKgcirDSIs0mtDkRoQzIDR9Bz
 *                         HsJNaYgfTGBSPbUkkZh824Jyn1tCATJIk4MfWrzzsFnZdmT4af2yoBnqu3FXGoDeGuQ+c4pHWj+m
 *                         0CcI1vJYG6qOHkxb++srCOBEGFrB6DHR8OX+xnZjNYZKPXUZVwdM2YnRFa9S86LL/WBhudkCAwEA
 *                         ATANBgkqhkiG9w0BAQsFAAOCAQEAZkw/qBt5Ait5sgeXFPC8NgrfEGQi+E20hZHZtqk2gNF+7Wsq
 *                         BQhTCYmsVeG2TYTLNUW0JBb8z+y9RZaAne/rLX+WudvGFu/nIDPoPAoB46cNT1RCoe2PWLrd1yWw
 *                         7mZm6ENU4/t/DiRDM62uayOSG8CA/5NdxVlpYxdgmdFGbCESBgpA/c6w2kKGrDE9354Q0T/0+Q4L
 *                         olq8RNFHDFYvVbpGL1TIfKvD7ovw/DaTcKIf79ioqiAiovmlrHYAy6u1sjWKRQgmcGdOEmGeddt0
 *                         oVDjEzgdkU7mZFX/m/mmKCDMiYJlAA7eOiXD/MEEAkBM1TKQbjO9c/vJDnS5WdYhuQ==</ds:X509Certificate>
 *                 </ds:X509Data>
 *             </ds:KeyInfo>
 *         </ds:Signature>
 *         <saml2:Subject>
 *             <saml2:NameID Format="urn:oasis:names:tc:SAML:2.0:nameid-format:transient">IDPAdmin</saml2:NameID>
 *             <saml2:SubjectConfirmation Method="urn:oasis:names:tc:SAML:2.0:cm:bearer">
 *                 <saml2:SubjectConfirmationData NotOnOrAfter="2025-06-12T16:10:56.128Z" Recipient="https://signin.aliyun.com/saml-role/sso"/>
 *             </saml2:SubjectConfirmation>
 *         </saml2:Subject>
 *         <saml2:Conditions NotBefore="2025-06-12T14:08:56.128Z" NotOnOrAfter="2025-06-12T16:10:56.128Z">
 *             <saml2:AudienceRestriction>
 *                 <saml2:Audience>urn:alibaba:cloudcomputing</saml2:Audience>
 *             </saml2:AudienceRestriction>
 *         </saml2:Conditions>
 *         <saml2:AttributeStatement>
 *             <saml2:Attribute Name="https://www.aliyun.com/SAML-Role/Attributes/RoleSessionName">
 *                 <saml2:AttributeValue>session_kewen-idp</saml2:AttributeValue>
 *             </saml2:Attribute>
 *             <saml2:Attribute Name="https://www.aliyun.com/SAML-Role/Attributes/SessionDuration">
 *                 <saml2:AttributeValue>3600</saml2:AttributeValue>
 *             </saml2:Attribute>
 *             <saml2:Attribute Name="https://www.aliyun.com/SAML-Role/Attributes/Role">
 *                 <saml2:AttributeValue>acs:ram::15557***214700:role/kewen-saml-role,acs:ram::15557***214700:saml-provider/kewen-saml</saml2:AttributeValue>
 *             </saml2:Attribute>
 *         </saml2:AttributeStatement>
 *         <saml2:AuthnStatement AuthnInstant="2025-06-12T14:10:56.128Z">
 *             <saml2:AuthnContext>
 *                 <saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Smartcard</saml2:AuthnContextClassRef>
 *             </saml2:AuthnContext>
 *         </saml2:AuthnStatement>
 *     </saml2:Assertion>
 * </saml2p:Response>
 *
 * @author kewen
 * @since 2025-06-10
 */
public class AliyunRoleSsoXmlResponse extends AbstractSimpleAttributeXmlResponse {
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

    public AliyunRoleSsoXmlResponse(String entityId, CertificateInfoStr certificateInfoStr, DateTime notAfter, String role, String username) {
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
    public String getNameID() {
        return username;
    }

    @Override
    public String getDestination() {
        return SSO_URL;
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
