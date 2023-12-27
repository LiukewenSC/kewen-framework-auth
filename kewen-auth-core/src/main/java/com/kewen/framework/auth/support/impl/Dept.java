package com.kewen.framework.auth.support.impl;

import com.kewen.framework.auth.support.AbstractAuthEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Dept extends AbstractAuthEntity<Long> {


    public Dept() {

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