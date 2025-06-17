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

import cn.hutool.crypto.PemUtil;
import com.kewen.framework.idaas.application.model.certificate.CertificateInfoStr;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

/**
 * 2025/06/17
 *
 * @author kewen
 * @since 4.23.0-mysql-aliyun-sovereign
 */
public class HutoolCertificateUtil {

    public static void exportPem(CertificateInfoStr certificateInfoStr, String type, OutputStream outputStream) {
        if ("PRIVATE".equals(type)) {
            PemUtil.writePemObject(type, certificateInfoStr.getPrivateKeyBase64Str().getBytes(), outputStream);
        } else if ("PUBLIC".equals(type)) {
            PemUtil.writePemObject(type, certificateInfoStr.getPublicKeyBase64Str().getBytes(), outputStream);
        } else if ("CERTIFICATE".equals(type)) {
            PemUtil.writePemObject(type, certificateInfoStr.getX509CertificateDerStr().getBytes(), outputStream);
        }
    }

    public static Key importPem(InputStream in) {
        return PemUtil.readPemKey(in);
    }
}
