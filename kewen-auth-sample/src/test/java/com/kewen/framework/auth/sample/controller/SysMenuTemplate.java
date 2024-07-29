package com.kewen.framework.auth.sample.controller;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuRoute;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

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
@TableName("sys_menu")
public class SysMenuTemplate extends SysMenuRoute {

    private List<SysMenuTemplate> children ;

}
