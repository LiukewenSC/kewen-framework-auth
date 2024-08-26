package com.kewen.framework.auth.rabc.composite.model;

import com.kewen.framework.auth.core.extension.AbstractIdNameFlagAuthEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Dept extends AbstractIdNameFlagAuthEntity<Long> {

    private static final long serialVersionUID = 6638347959892346419L;

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
