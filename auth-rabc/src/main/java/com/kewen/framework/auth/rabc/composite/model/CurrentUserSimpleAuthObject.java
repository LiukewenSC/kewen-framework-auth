package com.kewen.framework.auth.rabc.composite.model;


import java.util.List;

/**
 * 当前登录人的权限体，
 * @author kewen
 * @since 2024-09-05
 */
public class CurrentUserSimpleAuthObject  extends SimpleAuthObject {
    private static final long serialVersionUID = 842909202036381753L;

    /**
     * 填充用户的权限，如用户 有 role 和 dept ，那么用户也有 role+dept的组合权限
     */
    public void fillUserAuth(){
        List<Role> roles1 = getRoles();
        List<Dept> depts1 = getDepts();
        for (Dept dept : depts1) {
            for (Role role : roles1) {
                deptRoles.add(new DeptRole(dept,role));
            }
        }
    }
}
