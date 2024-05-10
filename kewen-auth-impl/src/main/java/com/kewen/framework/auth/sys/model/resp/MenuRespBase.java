package com.kewen.framework.auth.sys.model.resp;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kewen.framework.auth.sys.mp.entity.SysMenu;
import lombok.Data;

import com.kewen.framework.auth.sys.model.MenuTypeConstant;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 菜单返回结构
 *  {@link com.kewen.framework.auth.sys.mp.entity.SysMenu}
 * @author kewen
 * @since 2022-12-01 10:42
 */
@Data
public class MenuRespBase {

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
    @TableField("meta")
    protected Map<String, Object> meta;

    /**
     * 图片地址
     */
    @TableField("icon")
    protected String icon;

    /**
     * 类型： 1-菜单 2-按钮
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

    public void copyProperties(SysMenu sysMenu){
        BeanUtil.copyProperties(sysMenu,this,"meta");
        /*String menuMeta = sysMenu.getMeta();
        if(Objects.nonNull(menuMeta)){
            this.meta = JSONObject.parseObject(menuMeta);
        }*/
    }

}

