package com.kewen.framework.auth.support.entity;

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
public class User extends AbstractAuthEntity<Long> {


    public User() {

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
