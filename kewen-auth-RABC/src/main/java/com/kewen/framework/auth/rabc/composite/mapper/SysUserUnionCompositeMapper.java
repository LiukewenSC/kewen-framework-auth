package com.kewen.framework.auth.rabc.composite.mapper;

import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
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
     * @param dataId
     * @return
     */
    Integer hasAuth(@Param("auths") Collection<BaseAuth> auths, @Param("module") String module, @Param("operate") String operate, @Param("dataId") Long dataId);

    void selectMenuAuths();

    /**
     * 查找用户的角色、机构等
     * @param id
     * @return
     */
    SimpleAuthObject getUserAuthObject(@Param("id") Long id);
}
