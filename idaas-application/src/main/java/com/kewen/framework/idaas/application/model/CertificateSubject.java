package com.kewen.framework.idaas.application.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 证书信息
 * </p>
 *
 * @author kewen
 * @since 2025-06-09
 */
@Getter
@Setter
@Accessors(chain = true)
public class CertificateSubject {

    /**
     * 发行人
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
     * 证书内容
     */
    private String certificate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
