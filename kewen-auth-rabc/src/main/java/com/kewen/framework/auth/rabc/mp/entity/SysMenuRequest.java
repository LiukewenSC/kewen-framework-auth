package com.kewen.framework.auth.rabc.mp.entity;

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
 * 菜单表
 * </p>
 *
 * @author kewen
 * @since 2024-07-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_menu_request")
public class SysMenuRequest extends Model<SysMenuRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 请求名称
     */
    @TableField("name")
    private String name;

    /**
     * 请求路径
     */
    @TableField("path")
    private String path;

    /**
     * 父id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 权限类型 1-基于自身权限 2-基于父权限
     */
    @TableField("auth_type")
    private Integer authType;

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

    /**
     * 是否删除，默认0-未删除
     */
    @TableField("deleted")
    private Integer deleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
