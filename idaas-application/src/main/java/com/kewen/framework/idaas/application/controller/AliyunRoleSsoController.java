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
import com.kewen.framework.idaas.application.saml.util.ResponseUtil;
import com.kewen.framework.idaas.application.saml.util.SamlXmlObjectUtil;
import org.opensaml.saml.saml2.binding.decoding.impl.HTTPSOAP11Decoder;
import org.opensaml.saml.saml2.core.ArtifactResponse;
import org.opensaml.saml.saml2.core.Response;
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

    private static final String SSO_URL = "https://signin.aliyun.com/saml-role/sso";

    @GetMapping("/go")
    @ResponseBody
    public void goSso(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        // 跳转到阿里云角色sso
        Response response = SamlXmlObjectUtil.getResponse("kewen-entityId", SSO_URL);

        ResponseUtil.redirect(httpServletResponse, response, SSO_URL);

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

        ArtifactResponse artifactResponse = SamlXmlObjectUtil.getArtifactResponse(IDPConstants.IDP_ENTITY_ID, SSO_URL);

        ResponseUtil.redirect(resp, artifactResponse, SSO_URL);

    }

}
