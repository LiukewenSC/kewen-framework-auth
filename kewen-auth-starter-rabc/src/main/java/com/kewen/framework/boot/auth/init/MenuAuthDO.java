package com.kewen.framework.boot.auth.init;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MenuAuthDO {
    private String path;
    private String name;
}
