package com.kewen.framework.auth.sys.model.resp;

import com.kewen.framework.auth.extension.model.DefaultAuthObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuAuthResp extends MenuResp {

    private DefaultAuthObject authObject;

}
