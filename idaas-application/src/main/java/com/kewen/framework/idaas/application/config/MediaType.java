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

package com.kewen.framework.idaas.application.config;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2025/06/11
 *
 * @author kewen
 * @since 4.23.0-mysql-aliyun-sovereign
 */

public class MediaType {
    private static final String WILDCARD_TYPE = "*";
    private static final Pattern mediaTypePattern = Pattern.compile("^([^/]+)\\/([^ \t;]+)(?:[ \t]*;[ \t]*(.*))?$");
    private static final Pattern parameterPattern = Pattern.compile("([^; \\t\"=,]+)=((?:[^; \\t\"=,\\\\]+)|(?:(?:\"((?:[^\\\\\"]|\\\\.)+))\"))");

    private final String baseType, primaryType, subType;

    private final Map<String, String> parameters;
    private final String representation;

    public MediaType(String mediaType) {

        Matcher matcher = mediaTypePattern.matcher(mediaType);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("mediaType is not a valid media type");
        }

        this.primaryType = WILDCARD_TYPE.equals(matcher.group(1)) ? WILDCARD_TYPE : matcher.group(1);
        this.subType = WILDCARD_TYPE.equals(matcher.group(2)) ? WILDCARD_TYPE : matcher.group(2);

        if (primaryType == null || primaryType.isEmpty() || subType == null || subType.isEmpty()) {
            throw new IllegalArgumentException("mediaType is not a valid media type");
        }

        this.baseType = primaryType + "/" + subType;

        String parameterString = matcher.group(3);
        if (parameterString != null && !parameterString.isEmpty()) {

            LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
            matcher = parameterPattern.matcher(parameterString);
            while (matcher.find()) {
                String key = matcher.group(1);
                String escapedValue = matcher.group(3);
                String value = matcher.group(2);
                if (escapedValue != null) {
                    StringBuilder unescapedValue = new StringBuilder(escapedValue);
                    for (int i = 0; i < unescapedValue.length(); i++) {
                        if (unescapedValue.charAt(i) != '\\')
                            continue;
                        if (i + 1 == escapedValue.length())
                            throw new IllegalArgumentException("mediaType has invalid quoted parameter");
                        unescapedValue.deleteCharAt(i);
                    }
                    value = unescapedValue.toString();
                }
                String oldValue = parameters.put(key, value);
                if (oldValue != null) {
                    throw new IllegalArgumentException("mediaType has duplicate parameter");
                }
            }
            this.parameters = (parameters != null && !parameters.isEmpty()) ?
                    Collections.unmodifiableMap(new LinkedHashMap<String, String>(parameters)) :
                    Collections.<String, String>emptyMap();
        } else
            this.parameters = Collections.<String, String>emptyMap();
        this.representation = createRepresentation(this.baseType, this.parameters);

    }

    public MediaType(String primaryType, String subType) {
        this(primaryType, subType, null);
    }

    public MediaType(String primaryType, String subType, Map<String, String> parameters) {
        this.primaryType = WILDCARD_TYPE.equals(primaryType) ? WILDCARD_TYPE : primaryType;
        this.subType = WILDCARD_TYPE.equals(subType) ? WILDCARD_TYPE : subType;
        this.baseType = primaryType + "/" + subType;
        this.parameters = (parameters != null && !parameters.isEmpty()) ?
                Collections.unmodifiableMap(new LinkedHashMap<String, String>(parameters)) :
                Collections.<String, String>emptyMap();
        this.representation = createRepresentation(this.baseType, this.parameters);
    }


    private static String createRepresentation(String baseType, Map<String, String> parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseType);
        if (!parameters.isEmpty()) {
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                builder.append("; ");
                builder.append(parameter.getKey()).append('=');
                String value = parameter.getValue();
                StringBuilder escapedValue = new StringBuilder();
                boolean escaped = false;
                int i = 0;
                for (; i < value.length(); i++) {
                    char c = value.charAt(i);
                    if (c < 33 || c == '"' || c == '(' || c == ')' || c == ',' ||
                            c == '/' || c == ':' || c == ';' || c == '<' ||
                            c == '=' || c == '>' || c == '?' || c == '@' ||
                            c == '[' || c == ']' || c == '\\' || c == '{' ||
                            c == '}' || c == 127) {
                        escaped = true;
                        if (c == '\\' || c == '"')
                            escapedValue.append('\\');
                    }
                    escapedValue.append(c);
                }
                if (escaped)
                    builder.append('"').append(escapedValue).append('"');
                else
                    builder.append(value);
            }
        }
        return builder.toString();
    }

    public String getBaseType() {
        return baseType;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public String getSubType() {
        return subType;
    }

    public boolean isPrimaryTypeWildcard() {
        return primaryType.equals(WILDCARD_TYPE);
    }

    public boolean isSubTypeWildcard() {
        return subType.equals(WILDCARD_TYPE);
    }

    public boolean matchesBaseType(String contentType) {
        return matchesBaseType(new MediaType(contentType));
    }

    public boolean matchesBaseType(MediaType contentType) {
        return (isPrimaryTypeWildcard() || contentType.isPrimaryTypeWildcard() ||
                getPrimaryType().equals(contentType.getPrimaryType()) &&
                        (isSubTypeWildcard() || contentType.isSubTypeWildcard() ||
                                getSubType().equals(contentType.getSubType())));
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return representation;
    }

    /**
     * Retrieve the charset parameter, as a <code>Charset</code> object.
     *
     * @return The specified charset if any, otherwise <code>null</code>. No attempt
     * is made to apply a default charset, or to determine charset per the media-type
     * registration
     * @throws IllegalCharsetNameException "charset" was declared, but was a charset
     *                                     which is not known to the system.
     */
    public Charset getCharset() throws IllegalCharsetNameException {
        String charsetName = getParameter("charset");
        if (charsetName == null)
            return null;
        return Charset.forName(charsetName);
    }
}
