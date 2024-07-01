package com.kewen.framework.auth.rabc.model.resp;

import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.utils.TreeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class MenuAuthResp extends MenuRespBase  implements TreeUtil.TreeBase<MenuAuthResp,Long>{

    protected List<MenuAuthResp> children;

    private SimpleAuthObject authObject;

}
