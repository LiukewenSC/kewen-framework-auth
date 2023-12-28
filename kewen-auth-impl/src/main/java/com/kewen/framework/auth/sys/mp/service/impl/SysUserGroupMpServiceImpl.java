package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysUserGroup;
import com.kewen.framework.auth.sys.mp.mapper.SysUserGroupMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysUserGroupMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色组关联表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
@Primary
public class SysUserGroupMpServiceImpl extends ServiceImpl<SysUserGroupMpMapper, SysUserGroup> implements SysUserGroupMpService {

}
