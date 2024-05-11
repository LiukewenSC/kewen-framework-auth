package com.kewen.framework.auth.sys.model.resp;

import com.kewen.framework.auth.support.SimpleAuthObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuAuthResp extends MenuResp {

    private SimpleAuthObject authObject;

}
