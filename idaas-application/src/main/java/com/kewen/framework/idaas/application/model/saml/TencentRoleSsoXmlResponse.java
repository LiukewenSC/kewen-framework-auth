package com.kewen.framework.idaas.application.model.saml;

import com.kewen.framework.idaas.application.model.certificate.CertificateInfo;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfoStr;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯云角色SSO
 * <p>
 * <saml2p:Response xmlns:saml2p="urn:oasis:names:tc:SAML:2.0:protocol" Destination="https://cloud.tencent.com/login/saml" ID="_7b26a2c78bb866c330c95589ff0b9073" IssueInstant="2025-06-12T16:13:43.050Z" Version="2.0">
 * <saml2:Issuer xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion">kewen-idp</saml2:Issuer>
 * <saml2p:Status>
 * <saml2p:StatusCode Value="urn:oasis:names:tc:SAML:2.0:status:Success"/>
 * </saml2p:Status>
 * <saml2:Assertion xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion" ID="_be75c9f5d0ce28620373a7acc9591929" IssueInstant="2025-06-12T14:13:43.051Z" Version="2.0">
 * <saml2:Issuer>kewen-idp</saml2:Issuer>
 * <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
 * <ds:SignedInfo>
 * <ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
 * <ds:SignatureMethod Algorithm="http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"/>
 * <ds:Reference URI="#_be75c9f5d0ce28620373a7acc9591929">
 * <ds:Transforms>
 * <ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
 * <ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
 * </ds:Transforms>
 * <ds:DigestMethod Algorithm="http://www.w3.org/2001/04/xmlenc#sha256"/>
 * <ds:DigestValue>7irqpTSC0oXoNfi9sr1Q6d8gYRjgFuAVfU8R2+B5Eq4=</ds:DigestValue>
 * </ds:Reference>
 * </ds:SignedInfo>
 * <ds:SignatureValue>
 * euMkqNi8M+UdSshwjlEDfmMtYVIr7rkaHTZkjDSZ5/7wNBPHvUeV8RBMpdPOySBWe77pv87TmTjv&#13;
 * VkGyAX673XfxdWONm/sim+QRilKgYUeFv7gwf6Or2cyHZeBnJcV2MpeXm6UscTU/5Ot45YbVr4SL&#13;
 * CEkudx4FXScQLSFmcld+Mww6cD0GIXiH9/DIhitn9/SqM5iVwLR3dfGYVo0Jzjf0RAeATt29lpE+&#13;
 * j1hAdFF80wZObyzcfcfrA2yi3HCLbypqpBJYBtN09JGmE9xIIdRVDiyh0HReIexOQUEe/hqwthB0&#13;
 * HpcMLsCXJK309ycMk9atFwV+HEGiwxJkgGeyGA==
 * </ds:SignatureValue>
 * <ds:KeyInfo>
 * <ds:X509Data>
 * <ds:X509Certificate>MIIDEjCCAfqgAwIBAgIGAZdVZzFoMA0GCSqGSIb3DQEBCwUAMEoxETAPBgNVBAMMCEpvaG4gRG9l
 * MRQwEgYDVQQLDAtFbmdpbmVlcmluZzESMBAGA1UECgwJTXlDb21wYW55MQswCQYDVQQGEwJVUzAe
 * Fw0yNTA2MDkxNTU1MTRaFw0yNjA2MDkxNTU1MTRaMEoxETAPBgNVBAMMCEpvaG4gRG9lMRQwEgYD
 * VQQLDAtFbmdpbmVlcmluZzESMBAGA1UECgwJTXlDb21wYW55MQswCQYDVQQGEwJVUzCCASIwDQYJ
 * KoZIhvcNAQEBBQADggEPADCCAQoCggEBAI1EYFijS5y4UFB8+ji7IOK+LUC+HfqvF7fKv5FS5kaC
 * bg+Dl5HdR8O2DIxl3C948BfkYFOasCkuFk7Z0sQq7Hh+VyEgWjYrrGDDXXFFvMEilO5uE+RxfHGt
 * a6gVtqxYOSE2htoprN5STbL2ct3kP/H4fp1xTqdWajA4lh5fDv/sKgcirDSIs0mtDkRoQzIDR9Bz
 * HsJNaYgfTGBSPbUkkZh824Jyn1tCATJIk4MfWrzzsFnZdmT4af2yoBnqu3FXGoDeGuQ+c4pHWj+m
 * 0CcI1vJYG6qOHkxb++srCOBEGFrB6DHR8OX+xnZjNYZKPXUZVwdM2YnRFa9S86LL/WBhudkCAwEA
 * ATANBgkqhkiG9w0BAQsFAAOCAQEAZkw/qBt5Ait5sgeXFPC8NgrfEGQi+E20hZHZtqk2gNF+7Wsq
 * BQhTCYmsVeG2TYTLNUW0JBb8z+y9RZaAne/rLX+WudvGFu/nIDPoPAoB46cNT1RCoe2PWLrd1yWw
 * 7mZm6ENU4/t/DiRDM62uayOSG8CA/5NdxVlpYxdgmdFGbCESBgpA/c6w2kKGrDE9354Q0T/0+Q4L
 * olq8RNFHDFYvVbpGL1TIfKvD7ovw/DaTcKIf79ioqiAiovmlrHYAy6u1sjWKRQgmcGdOEmGeddt0
 * oVDjEzgdkU7mZFX/m/mmKCDMiYJlAA7eOiXD/MEEAkBM1TKQbjO9c/vJDnS5WdYhuQ==</ds:X509Certificate>
 * </ds:X509Data>
 * </ds:KeyInfo>
 * </ds:Signature>
 * <saml2:Subject>
 * <saml2:NameID Format="urn:oasis:names:tc:SAML:2.0:nameid-format:transient">IDPAdmin</saml2:NameID>
 * <saml2:SubjectConfirmation Method="urn:oasis:names:tc:SAML:2.0:cm:bearer">
 * <saml2:SubjectConfirmationData NotOnOrAfter="2025-06-12T16:13:43.050Z" Recipient="https://cloud.tencent.com/login/saml"/>
 * </saml2:SubjectConfirmation>
 * </saml2:Subject>
 * <saml2:Conditions NotBefore="2025-06-12T14:11:43.051Z" NotOnOrAfter="2025-06-12T16:13:43.050Z">
 * <saml2:AudienceRestriction>
 * <saml2:Audience>cloud.tencent.com</saml2:Audience>
 * </saml2:AudienceRestriction>
 * </saml2:Conditions>
 * <saml2:AttributeStatement>
 * <saml2:Attribute Name="https://cloud.tencent.com/SAML/Attributes/Role">
 * <saml2:AttributeValue>qcs::cam::uin/100011725657:roleName/KewenSamlRole,qcs::cam::uin/100011725657:saml-provider/KewenSamlTest</saml2:AttributeValue>
 * </saml2:Attribute>
 * <saml2:Attribute Name="https://cloud.tencent.com/SAML/Attributes/RoleSessionName">
 * <saml2:AttributeValue>session_kewen-idp</saml2:AttributeValue>
 * </saml2:Attribute>
 * </saml2:AttributeStatement>
 * <saml2:AuthnStatement AuthnInstant="2025-06-12T14:13:43.051Z">
 * <saml2:AuthnContext>
 * <saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Smartcard</saml2:AuthnContextClassRef>
 * </saml2:AuthnContext>
 * </saml2:AuthnStatement>
 * </saml2:Assertion>
 * </saml2p:Response>
 */
public class TencentRoleSsoXmlResponse extends AbstractSimpleAttributeXmlResponse {

    private final String entityId;
    private final String username;
    private final CertificateInfoStr certificateInfoStr;
    private final String role;
    private final DateTime notAfter;

    public TencentRoleSsoXmlResponse(String entityId, String username, CertificateInfoStr certificateInfoStr, String role, DateTime notAfter) {
        this.entityId = entityId;
        this.username = username;
        this.certificateInfoStr = certificateInfoStr;
        this.role = role;
        this.notAfter = notAfter;
    }

    @Override
    protected Map<String, String> getAttributeValues() {
        HashMap<String, String> map = new HashMap<>();
        map.put("https://cloud.tencent.com/SAML/Attributes/RoleSessionName", "session_" + entityId);
        map.put("https://cloud.tencent.com/SAML/Attributes/Role", role);
        return map;
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
        return "https://cloud.tencent.com/login/saml";
    }

    @Override
    protected CertificateInfo getCertificateInfo() {
        return certificateInfoStr.parseCertificateInfo();
    }

    @Override
    protected DateTime notAfter() {
        return notAfter;
    }

    @Override
    protected String getAudienceURI() {
        return "cloud.tencent.com";
    }
}
