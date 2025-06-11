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

/**
 * 2025/06/11
 *
 * @author kewen
 * @since 4.23.0-mysql-aliyun-sovereign
 */
public enum HashAlgorithm {
    MD5,
    SHA1,
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    private String jceName;

    private HashAlgorithm() {
        jceName = toString();
    }

    private HashAlgorithm(String jceName) {
        this.jceName = jceName;
    }

    public String getJceName() {
        return jceName;
    }
}
