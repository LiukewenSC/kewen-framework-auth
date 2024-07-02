package com.kewen.framework.auth.rabc.model.resp;



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
public class MenuResp extends MenuRespBase implements TreeUtil.TreeBase<MenuResp,Long>{

    protected List<MenuResp> children;

}
