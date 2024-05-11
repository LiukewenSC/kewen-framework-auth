package com.kewen.framework.auth.support.model;

import com.kewen.framework.auth.extension.AbstractIdNameAuthEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
public class User extends AbstractIdNameAuthEntity<Long> {


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
