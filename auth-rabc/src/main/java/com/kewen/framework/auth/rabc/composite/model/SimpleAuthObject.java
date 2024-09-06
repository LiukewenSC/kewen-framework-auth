package com.kewen.framework.auth.rabc.composite.model;


import com.kewen.framework.auth.core.extension.AbstractAuthObject;
import com.kewen.framework.auth.core.extension.IFlagAuthEntity;
import com.kewen.framework.auth.core.model.BaseAuth;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * 权限对象集合体，设置权限的时候此传入此结构体
 * @author kewen
 * @since 2023-12-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SimpleAuthObject extends AbstractAuthObject {

    private static final long serialVersionUID = 842909202036381753L;
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

    /**
     * 部门角色集合
     */
    protected List<DeptRole> deptRoles = new ArrayList<>();

    public void addUsers(User... users){
        this.users.addAll(Arrays.asList(users));
    }
    public void addDepts(Dept... depts){
        this.depts.addAll(Arrays.asList(depts));
    }
    public void addRoles(Role... roles){
        this.roles.addAll(Arrays.asList(roles));
    }
    public void addDeptRoles(DeptRole... roles){
        this.deptRoles.addAll(Arrays.asList(roles));
    }


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
        if (deptRoles != null) {
            for (DeptRole deptRole : deptRoles) {
                baseAuths.add(deptRole.getAuth());
            }
        }
        addAnotherBashAuth(baseAuths);
        return baseAuths;
    }

    /**
     * 添加其他权限对象，若子类继承可以扩展这里，也可以覆写listBaseAuth()
     * @param baseAuths
     */
    protected void addAnotherBashAuth(Collection<BaseAuth>  baseAuths){

    }

    @Override
    protected void setBaseAuth(List<IFlagAuthEntity> abstractAuthEntities) {
        for (IFlagAuthEntity abstractAuthEntity : abstractAuthEntities) {
            if (abstractAuthEntity instanceof User) {
                users.add(((User) abstractAuthEntity));
            } else if (abstractAuthEntity instanceof Role) {
                roles.add(((Role) abstractAuthEntity));
            } else if (abstractAuthEntity instanceof Dept) {
                depts.add(((Dept) abstractAuthEntity));
            } else if (abstractAuthEntity instanceof DeptRole){
                deptRoles.add(((DeptRole) abstractAuthEntity));
            }else {
                setAnotherBaseAuth(abstractAuthEntity);
            }
        }
    }

    /**
     * 设置其他权限对象，若子类需要的话可以扩展这里，也可以覆写 setBaseAuth()
     * @param abstractAuthEntity
     */
    protected void setAnotherBaseAuth(IFlagAuthEntity abstractAuthEntity){

    }
}
