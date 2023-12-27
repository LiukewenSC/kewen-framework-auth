package com.kewen.framework.auth.support;


import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.support.impl.DefaultAuthObject;
import com.kewen.framework.auth.support.impl.Dept;
import com.kewen.framework.auth.support.impl.Role;
import com.kewen.framework.auth.support.impl.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class AbstractAuthObjectTest {

    @Test
    public  void t1(){
        DefaultAuthObject.scanPackages("com.kewen");
        DefaultAuthObject object = new DefaultAuthObject();
        object.setUsers(Arrays.asList(new User(1L,"用户1"),new User(2L,"用户2"),new User(3L,"用户3")));
        object.setDepts(Arrays.asList(new Dept(1L,"部门1"),new Dept(2L,"部门2"),new Dept(3L,"部门3")));
        object.setRoles(Arrays.asList(new Role(1L,"角色1"),new Role(2L,"角色2"),new Role(3L,"角色3")));

        Collection<BaseAuth> baseAuths = object.listBaseAuth();
        Assert.assertEquals(baseAuths.size(),9);


        DefaultAuthObject defaultAuthObject = new DefaultAuthObject();

        defaultAuthObject.setPropertiesFromBaseAuth(baseAuths);
        Assert.assertNotNull(defaultAuthObject.getUsers().get(0).id);
        Assert.assertNotNull(defaultAuthObject.getDepts().get(0).id);
        Assert.assertNotNull(defaultAuthObject.getRoles().get(0).id);
        System.out.println(defaultAuthObject);
    }

}