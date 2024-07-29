package com.kewen.framework.auth.rabc.model.resp;



import com.kewen.framework.auth.rabc.mp.entity.SysMenuRequest;
import com.kewen.framework.auth.rabc.utils.BeanUtil;
import com.kewen.framework.auth.rabc.utils.TreeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @descrpition
 * {@link SysMenuRequest}
 * @author kewen
 * @since 2022-12-01 10:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuRequestResp extends SysMenuRequest implements TreeUtil.TreeBase<MenuRequestResp,Long>{

    protected List<MenuRequestResp> children;

    public SysMenuRequest toSysMenuRequest() {
        return BeanUtil.toBean(this, SysMenuRequest.class);
    }

}
