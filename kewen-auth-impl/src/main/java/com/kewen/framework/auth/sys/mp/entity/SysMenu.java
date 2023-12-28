package com.kewen.framework.auth.sys.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_menu")
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名
     */
    @TableField("name")
    private String name;

    /**
     * 父id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 链接或路径
     */
    @TableField("url")
    private String url;

    /**
     * 图片地址
     */
    @TableField("image")
    private String image;

    /**
     * 类型： 1-菜单 2-按钮 3-外部链接
     */
    @TableField("type")
    private Integer type;

    /**
     * 权限类型 1-基于父菜单权限 2-基于本身权限
     */
    @TableField("auth_type")
    private Integer authType;

    /**
     * 描述
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
