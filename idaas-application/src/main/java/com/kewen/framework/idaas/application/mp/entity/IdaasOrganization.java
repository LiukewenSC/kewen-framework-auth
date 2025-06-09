package com.kewen.framework.idaas.application.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 组织信息
 * </p>
 *
 * @author kewen
 * @since 2025-06-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("idaas_organization")
public class IdaasOrganization {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 机构名
     */
    @TableField("name")
    private String name;

    /**
     * 国家
     */
    @TableField("c")
    private String c;

    /**
     * 州/省
     */
    @TableField("st")
    private String st;

    /**
     * 地名
     */
    @TableField("l")
    private String l;

    /**
     * 组织
     */
    @TableField("o")
    private String o;

    /**
     * 组织单位
     */
    @TableField("ou")
    private String ou;

    /**
     * 通用名称
     */
    @TableField("cn")
    private String cn;

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
