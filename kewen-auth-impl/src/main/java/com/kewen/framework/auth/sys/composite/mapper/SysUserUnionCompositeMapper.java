package com.kewen.framework.auth.sys.composite.mapper;

import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.support.SimpleAuthObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * @author kewen
 * @descrpition
 * @since 2023-04-07
 */
@Mapper
public interface SysUserUnionCompositeMapper {


    /**
     * 用户是否有数据权限
     * @param auths
     * @param module
     * @param operate
     * @param businessId
     * @return
     */
    Integer hasAuth(@Param("auths") Collection<BaseAuth> auths, @Param("module") String module, @Param("operate") String operate, @Param("businessId") Long businessId);

    void selectMenuAuths();

    /**
     * 查找用户的角色、机构等
     * @param id
     * @return
     */
    SimpleAuthObject getUserAuthObject(@Param("id") Long id);
}
