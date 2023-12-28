package com.kewen.framework.auth.extension.model;

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
}
