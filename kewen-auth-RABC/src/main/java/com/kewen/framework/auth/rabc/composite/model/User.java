package com.kewen.framework.auth.rabc.composite.model;

import com.kewen.framework.auth.core.extension.AbstractIdNameFlagAuthEntity;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
public class User extends AbstractIdNameFlagAuthEntity<Long> {


    public User() {
        super();
    }

    public User(Long id , String name) {
        this.id=id;
        this.name=name;
    }


    @Override
    public String flag() {
        return "USER";
    }

    @Override
    public void setId(Long aLong) {
        super.setId(aLong);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getName() {
        return super.getName();
    }
}
