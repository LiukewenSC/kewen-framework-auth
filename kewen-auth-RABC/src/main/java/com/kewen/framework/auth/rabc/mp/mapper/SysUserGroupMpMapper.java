package com.kewen.framework.auth.rabc.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kewen.framework.auth.rabc.mp.entity.SysUserGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户角色组关联表 Mapper 接口
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Mapper
public interface SysUserGroupMpMapper extends BaseMapper<SysUserGroup> {

}
