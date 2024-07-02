package com.kewen.framework.auth.rabc.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.kewen.framework.auth.rabc.model.MenuTypeConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author kewen
 * @since 2024-05-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "sys_menu",autoResultMap = true)
public class SysMenu extends Model<SysMenu> {

    protected static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    protected Long id;

    /**
     * 菜单名
     */
    @TableField("name")
    protected String name;

    /**
     * 父id
     */
    @TableField("parent_id")
    protected Long parentId;

    /**
     * 链接或路径
     */
    @TableField("path")
    protected String path;

    /**
     * 重定向路由
     */
    @TableField("redirect")
    protected String redirect;

    /**
     * 组件名
     */
    @TableField("component")
    protected String component;

    /**
     * 元信息
     */
    @TableField(value = "meta",typeHandler = JacksonTypeHandler.class)
    protected Map<String, Object> meta;

    /**
     * 图片地址
     */
    @TableField("icon")
    protected String icon;

    @TableField("hidden")
    protected Boolean hidden;

    /**
     * 类型： 1-菜单 2-按钮 3-外部链接
     * {@link MenuTypeConstant.TYPE}
     */
    @TableField("type")
    protected Integer type;

    /**
     * 权限类型 1-基于父菜单权限 2-基于本身权限
     */
    @TableField("auth_type")
    protected Integer authType;

    /**
     * 描述
     */
    @TableField("description")
    protected String description;

    /**
     * 创建时间
     */
    @TableField("create_time")
    protected LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    protected LocalDateTime updateTime;

    /**
     * 是否删除，默认0-未删除
     */
    @TableField("deleted")
    protected Integer deleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
