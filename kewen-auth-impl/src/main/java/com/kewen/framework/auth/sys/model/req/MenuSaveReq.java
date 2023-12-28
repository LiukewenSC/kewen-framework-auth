package com.kewen.framework.auth.sys.model.req;

import com.kewen.framework.auth.extension.model.DefaultAuthObject;
import com.kewen.framework.auth.sys.mp.entity.SysMenu;
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
    DefaultAuthObject authObject;
}
