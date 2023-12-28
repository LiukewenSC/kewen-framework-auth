package com.kewen.framework.auth.sys.model.resp;


import com.kewen.framework.auth.extension.model.DefaultAuthObject;
import com.kewen.framework.auth.sys.model.SysAuthorityObject;
import com.kewen.framework.auth.util.TreeUtil;
import com.kewen.framework.common.core.utils.TreeUtil;
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
public class MenuResp extends MenuRespBase implements TreeUtil.TreeBase<MenuResp,Long>{

    private DefaultAuthObject authObject;

    private List<MenuResp> children;


    @Override
    public void setChildren(List<MenuResp> children) {
        this.children = children;
    }

    public List<MenuResp> getChildren() {
        return children;
    }
}
