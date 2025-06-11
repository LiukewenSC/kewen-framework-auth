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

package com.kewen.framework.idaas.application.util;

import com.kewen.framework.idaas.application.model.AbstractXmlResponse;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.encoder.MessageEncodingException;
import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.common.messaging.context.SAMLEndpointContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.binding.encoding.impl.HTTPPostSimpleSignEncoder;
import org.opensaml.saml.saml2.core.StatusResponseType;
import org.opensaml.saml.saml2.metadata.Endpoint;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;
import org.opensaml.saml.saml2.metadata.impl.SingleSignOnServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * 2025/05/14
 *
 * @author kewen
 * @since 4.23.0-mysql-aliyun-sovereign
 */
public class ResponseUtil {

    private static VelocityEngine velocityEngine;

    static {
        VelocityEngine engine = new VelocityEngine();
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty("input.encoding", "UTF-8");
        props.setProperty("output.encoding", "UTF-8");
        engine.init(props);
        velocityEngine = engine;
    }

    private static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);

    public static void redirect(HttpServletResponse httpServletResponse, AbstractXmlResponse abstractXmlResponse) {
        StatusResponseType statusResponseType;
        if (abstractXmlResponse.isEncryptResponse()) {
            statusResponseType = abstractXmlResponse.getArtifactResponse();
        } else {
            statusResponseType = abstractXmlResponse.getResponse();
        }
        redirect(httpServletResponse, statusResponseType);
    }

    public static void redirect(HttpServletResponse httpServletResponse, StatusResponseType message) {
        String destination = message.getDestination();
        MessageContext<SAMLObject> context = new MessageContext<>();

        context.setMessage(message);
        //SAMLBindingSupport.setRelayState(context, "relayState");

        //关于传输对端实体的信息，对于IDP就是SP，对于SP就是IDP；
        SAMLPeerEntityContext peerEntityContext =
                context.getSubcontext(SAMLPeerEntityContext.class, true);

        //端点信息；
        SAMLEndpointContext endpointContext =
                peerEntityContext.getSubcontext(SAMLEndpointContext.class, true);
        endpointContext.setEndpoint(getIPDEndpoint(destination));

        /*//数据签名环境上线文
        SignatureSigningParameters signatureSigningParameters = new SignatureSigningParameters();
        //获得证书，其中包含公钥
        signatureSigningParameters.setSigningCredential(SPCredentials.getCredential());
        //ALGO_ID_SIGNATURE_RSA_SHA256
        signatureSigningParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);


        context.getSubcontext(SecurityParametersContext.class, true)
                .setSignatureSigningParameters(signatureSigningParameters);*/


        // OpenSAML提供了HTTPRedirectDefalteEncoder
        // 它将帮助我们来对于AuthnRequest进行序列化和签名
        // redirect方式
        //BaseSAML2MessageEncoder encoder = new HTTPRedirectDeflateEncoder();
        // post方式
        HTTPPostSimpleSignEncoder encoder = new HTTPPostSimpleSignEncoder();

        encoder.setVelocityEngine(velocityEngine);
        encoder.setMessageContext(context);
        encoder.setHttpServletResponse(httpServletResponse);
        log.info("AuthnRequest: ");
        OpenSAMLUtils.logSAMLObject(message);
        log.info("Redirecting to IDP");
        try {
            encoder.prepareContext();
            //*encode*方法将会压缩消息，生成签名，添加结果到URL并从定向用户到Idp.
            //先使用RFC1951作为默认方法压缩数据，在对压缩后的数据信息Base64编码
            encoder.initialize();
            encoder.encode();
        } catch (MessageEncodingException | ComponentInitializationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Endpoint getIPDEndpoint(String locationURL) {
        SingleSignOnService endpoint = new SingleSignOnServiceBuilder().buildObject();
        endpoint.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
        endpoint.setLocation(locationURL);

        return endpoint;
    }

}
