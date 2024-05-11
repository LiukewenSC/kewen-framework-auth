package com.kewen.framework.auth.support.model;

import com.kewen.framework.auth.extension.AbstractIdNameAuthEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Dept extends AbstractIdNameAuthEntity<Long> {


    public Dept() {
        super();
    }

    public Dept(Long id , String name) {
        this.id=id;
        this.name=name;
    }


    /**
     * 权限标记
     * @return
     */
    @Override
    public String flag() {
        return "DEPT";
    }
}
