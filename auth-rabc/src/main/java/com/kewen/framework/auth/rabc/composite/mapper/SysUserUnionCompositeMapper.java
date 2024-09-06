package com.kewen.framework.auth.rabc.composite.mapper;

import com.kewen.framework.auth.rabc.composite.model.CurrentUserSimpleAuthObject;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author kewen
 * @descrpition
 * @since 2023-04-07
 */
@Mapper
public interface SysUserUnionCompositeMapper {

    /**
     * 查找用户的角色、机构等
     * @param id
     * @return
     */
    CurrentUserSimpleAuthObject getUserAuthObject(@Param("id") Long id);
}
