package com.kewen.framework.auth.rabc.model.resp;



import com.kewen.framework.auth.rabc.mp.entity.SysMenuRoute;
import com.kewen.framework.auth.rabc.utils.BeanUtil;
import com.kewen.framework.auth.rabc.utils.TreeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @descrpition
 * @author kewen
 * @since 2022-12-01 10:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuRouteResp extends SysMenuRoute implements TreeUtil.TreeBase<MenuRouteResp,Long>{

    protected List<MenuRouteResp> children;

    public static MenuRouteResp from(SysMenuRoute sysMenuRoute) {
        return BeanUtil.toBean(sysMenuRoute, MenuRouteResp.class);
    }

}
