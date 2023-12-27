package com.kewen.framework.auth.support.impl;


import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.support.AbstractAuthEntity;
import com.kewen.framework.auth.support.AbstractAuthObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 权限对象集合，设置权限的时候此传入此结构体
 * @author kewen
 * @since 2023-12-27
 */
@Data
public class DefaultAuthObject extends AbstractAuthObject {

    /**
     * 用户集合
     */
    protected List<User> users = new ArrayList<>();
    /**
     * 角色集合
     */
    protected List<Role> roles = new ArrayList<>();
    /**
     * 部门集合
     */
    protected List<Dept> depts = new ArrayList<>();

    @Override
    public Collection<BaseAuth> listBaseAuth() {
        Collection<BaseAuth> baseAuths = new HashSet<>();
        if (users != null) {
            for (User user : users) {
                baseAuths.add(user.getAuth());
            }
        }
        if (roles != null) {
            for (Role role : roles) {
                baseAuths.add(role.getAuth());
            }
        }
        if (depts != null) {
            for (Dept dept : depts) {
                baseAuths.add(dept.getAuth());
            }
        }
        return baseAuths;
    }

    @Override
    protected void setBaseAuth(List<AbstractAuthEntity> abstractAuthEntities) {
        for (AbstractAuthEntity abstractAuthEntity : abstractAuthEntities) {
            if (abstractAuthEntity instanceof User) {
                users.add(((User) abstractAuthEntity));
            } else if (abstractAuthEntity instanceof Role) {
                roles.add(((Role) abstractAuthEntity));
            } else if (abstractAuthEntity instanceof Dept) {
                depts.add(((Dept) abstractAuthEntity));
            }
        }
    }
}
