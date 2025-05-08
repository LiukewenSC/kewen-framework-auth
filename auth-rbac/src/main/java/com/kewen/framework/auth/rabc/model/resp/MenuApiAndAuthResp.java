package com.kewen.framework.auth.rabc.model.resp;

import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuApi;
import com.kewen.framework.auth.rabc.utils.BeanUtil;
import com.kewen.framework.auth.rabc.utils.TreeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class MenuApiAndAuthResp extends SysMenuApi implements TreeUtil.TreeBase<MenuApiAndAuthResp,Long>{

    protected List<MenuApiAndAuthResp> children;

    private SimpleAuthObject authObject;

    public static MenuApiAndAuthResp of(SysMenuApi sysMenuRequest) {
        return BeanUtil.toBean(sysMenuRequest, MenuApiAndAuthResp.class);
    }

}
