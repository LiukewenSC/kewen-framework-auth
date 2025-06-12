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

package com.kewen.framework.idaas.application.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 2025/06/12
 *
 * @author kewen
 */
@Data
@Accessors(chain = true)
public class CertificateResp {
    private Long id;
    private CertificateInfoStr certificateInfoStr;
}
