package com.kewen.framework.auth.rabc.model.req;

import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.mp.entity.SysMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author kewen
 * @since 2023-04-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuSaveReq extends SysMenu {
    SimpleAuthObject authObject;
}
