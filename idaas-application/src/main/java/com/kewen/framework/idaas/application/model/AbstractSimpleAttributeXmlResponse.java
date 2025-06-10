package com.kewen.framework.idaas.application.model;

import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.core.xml.schema.impl.XSAnyBuilder;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeValue;
import org.opensaml.saml.saml2.core.impl.AttributeBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简单的属性返回对象
 *
 * @author kewen
 * @since 2025-06-10
 */
public abstract class AbstractSimpleAttributeXmlResponse extends AbstractXmlResponse {


    /**
     * 填属性值即可，
     * 如 acs:ram::1555734646214700:role/old-idp-role,acs:ram::1555734646214700:saml-provider/old-idp
     *
     * @return
     */
    protected abstract Map<String, String> getAttributeValues();

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
    public List<Attribute> getAttributes() {
        Map<String, String> attributeValues = getAttributeValues();
        return attributeValues.entrySet().stream().map(entry -> {
            Attribute attribute = new AttributeBuilder().buildObject();
            attribute.setName(entry.getKey());
            XSAny attrValue = new XSAnyBuilder().buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
            attrValue.setTextContent(entry.getValue());
            attribute.getAttributeValues().add(attrValue);
            return attribute;
        }).collect(Collectors.toList());
    }

}
