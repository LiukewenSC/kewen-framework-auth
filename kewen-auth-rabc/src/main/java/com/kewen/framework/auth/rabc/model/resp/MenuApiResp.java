package com.kewen.framework.auth.rabc.model.resp;



import com.kewen.framework.auth.rabc.mp.entity.SysMenuApi;
import com.kewen.framework.auth.rabc.utils.BeanUtil;
import com.kewen.framework.auth.rabc.utils.TreeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @descrpition
 * {@link SysMenuApi}
 * @author kewen
 * @since 2022-12-01 10:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuApiResp extends SysMenuApi implements TreeUtil.TreeBase<MenuApiResp,Long>{

    protected List<MenuApiResp> children;

    public SysMenuApi toSysMenuRequest() {
        return BeanUtil.toBean(this, SysMenuApi.class);
    }

}
