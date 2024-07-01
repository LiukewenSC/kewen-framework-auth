package extension;


import com.kewen.framework.auth.core.model.AuthConstant;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.model.Dept;
import com.kewen.framework.auth.rabc.composite.model.Role;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.composite.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class AbstractAuthObjectTest {

    /**
     * 测试AuthObject和BaseAuth的转换
     */
    @Test
    public  void testAuthObject2BaseAuth(){
        SimpleAuthObject object = new SimpleAuthObject();
        AuthConstant.AUTH_SPLIT="__";
        object.setUsers(Arrays.asList(new User(1L,"用户1"),new User(2L,"用户2"),new User(3L,"用户3")));
        object.setDepts(Arrays.asList(new Dept(1L,"部门1"),new Dept(2L,"部门2"),new Dept(3L,"部门3")));
        object.setRoles(Arrays.asList(new Role(1L,"角色1"),new Role(2L,"角色2"),new Role(3L,"角色3")));

        Collection<BaseAuth> baseAuths = object.listBaseAuth();
        Assert.assertEquals(baseAuths.size(),9);


        SimpleAuthObject simpleAuthObject = new SimpleAuthObject();

        simpleAuthObject.setProperties(baseAuths);
        Assert.assertNotNull(simpleAuthObject.getUsers().get(0).getId());
        Assert.assertNotNull(simpleAuthObject.getDepts().get(0).getId());
        Assert.assertNotNull(simpleAuthObject.getRoles().get(0).getId());
        System.out.println(simpleAuthObject);
    }

}