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

package com.kewen.framework.idaas.application.model.resp;

import com.kewen.framework.idaas.application.model.certificate.CertificateInfoStr;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 2025/06/12
 *
 * @author kewen
 */
@Data
@Accessors(chain = true)
public class CertificateResp {

    /**
     * 主键
     */
    private Long id;

    /**
     * 发行人
     */
    private String issuer;

    /**
     * 主体
     */
    private String subject;

    /**
     * 生效时间
     */
    private Date effectTime;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 证书信息
     */
    private CertificateInfoStr certificateInfoStr;

}
