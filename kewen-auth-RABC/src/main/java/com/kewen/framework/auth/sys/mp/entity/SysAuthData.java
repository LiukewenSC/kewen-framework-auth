package com.kewen.framework.auth.sys.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用权限表
 * </p>
 *
 * @author kewen
 * @since 2024-05-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_auth_data")
public class SysAuthData extends Model<SysAuthData> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模块
     */
    @TableField("module")
    private String module;

    /**
     * 业务ID 应用中业务的主键ID
     */
    @TableField("business_id")
    private Long businessId;

    /**
     * 操作类型 unified modify delete 等,应用可以自定义操作名称
     */
    @TableField("operate")
    private String operate;

    /**
     * 权限字符串
     */
    @TableField("authority")
    private String authority;

    /**
     * 权限描述
     */
    @TableField("description")
    private String description;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
