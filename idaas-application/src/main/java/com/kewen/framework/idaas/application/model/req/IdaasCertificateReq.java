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

package com.kewen.framework.idaas.application.model.req;

import lombok.Data;

import java.util.Date;

/**
 * 2025/06/12
 *
 * @author kewen
 * @since 4.23.0-mysql-aliyun-sovereign
 */
@Data
public class IdaasCertificateReq {
    protected String subject = "CN=John Doe, OU=Engineering, O=MyCompany, C=US";
    protected String issuer = "CN=John Doe, OU=Engineering, O=MyCompany, C=US";
    protected String serial;
    protected Date notBefore = new Date(System.currentTimeMillis());
    protected Date notAfter = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365));
    protected String signatureAlgorithm = "SHA256withRSA";

}
