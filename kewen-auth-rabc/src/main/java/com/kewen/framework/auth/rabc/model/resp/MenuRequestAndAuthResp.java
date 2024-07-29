package com.kewen.framework.auth.rabc.model.resp;

import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuRequest;
import com.kewen.framework.auth.rabc.utils.BeanUtil;
import com.kewen.framework.auth.rabc.utils.TreeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class MenuRequestAndAuthResp extends SysMenuRequest implements TreeUtil.TreeBase<MenuRequestAndAuthResp,Long>{

    protected List<MenuRequestAndAuthResp> children;

    private SimpleAuthObject authObject;

    public static MenuRequestAndAuthResp of(SysMenuRequest sysMenuRequest) {
        return BeanUtil.toBean(sysMenuRequest, MenuRequestAndAuthResp.class);
    }

}
