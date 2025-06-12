package com.kewen.framework.idaas.application.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

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
@TableName("idaas_certificate")
public class IdaasCertificate {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发行人
     */
    @TableField("issuer")
    private String issuer;

    /**
     * 主体
     */
    @TableField("subject")
    private String subject;

    /**
     * 证书内容
     */
    @TableField("certificate")
    private String certificate;
    /**
     * 签名算法
     */
    @TableField("signature_algorithm")
    private String signatureAlgorithm;
    /**
     * 私钥
     */
    @TableField("private_key")
    private String privateKey;

    /**
     * 公钥
     */
    @TableField("public_key")
    private String publicKey;

    /**
     * 生效时间
     */
    @TableField("effect_time")
    private Date effectTime;
    /**
     * 过期时间
     */
    @TableField("expire_time")
    private Date expireTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @TableField("deleted")
    private Integer deleted;


}
