package com.kewen.framework.auth.rabc.extension;

import cn.hutool.core.util.IdUtil;
import com.kewen.framework.auth.core.annotation.menu.MenuApiEntity;
import com.kewen.framework.auth.core.annotation.menu.MenuApiServcie;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuApi;
import com.kewen.framework.auth.rabc.mp.service.SysMenuApiMpService;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于RABC的菜单API存储
 * @author kewen
 * @since 2024-08-09
 */
@Setter
public class RabcMenuApiServcie implements MenuApiServcie<Long> {


    SysMenuApiMpService sysMenuApiMpService;

    @Override
    public List<MenuApiEntity<Long>> list() {
        List<SysMenuApi> list = sysMenuApiMpService.list();
        return list.stream().map(l-> new MenuApiEntity<Long>()
                .setId(l.getId())
                .setName(l.getName())
                .setPath(l.getPath())
                .setParentId(l.getParentId())
        ).collect(Collectors.toList());
    }

    @Override
    public void saveBatch(List<MenuApiEntity<Long>> menuApiEntities) {
        List<SysMenuApi> list = menuApiEntities.stream().map(m -> new SysMenuApi()
                        .setId(m.getId())
                        .setName(m.getName())
                        .setPath(m.getPath())
                        .setParentId(m.getParentId())
                        .setAuthType(fillAuthType(m.getParentId())))
                .collect(Collectors.toList());
        sysMenuApiMpService.saveBatch(list);
    }
    //填充类型， 父ID为0 则填充自己
    public Integer fillAuthType(Long parentId){
        return (parentId==null || parentId == 0L) ? 1 : 2;
    }
    @Override
    public Long getRootParentId() {
        return 0L;
    }

    /**
     * 雪花算法生成id
     * @return
     */
    @Override
    public Long generateId() {
        return IdUtil.getSnowflakeNextId();
    }
}
