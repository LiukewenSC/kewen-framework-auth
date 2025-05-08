package com.kewen.framework.auth.rabc.composite.model;

import com.kewen.framework.auth.core.entity.AbstractIdNameFlagAuthEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Role extends AbstractIdNameFlagAuthEntity<Long> {


    private static final long serialVersionUID = 7783176371589001528L;

    public Role() {
        super();
    }

    public Role(Long id , String name) {
        this.id=id;
        this.name=name;
    }


    @Override
    public String flag() {
        return "ROLE";
    }
}
