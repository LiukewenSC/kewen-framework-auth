package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysGroupRole;
import com.kewen.framework.auth.sys.mp.mapper.SysGroupRoleMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysGroupRoleMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色组角色关联表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
@Primary
public class SysGroupRoleMpServiceImpl extends ServiceImpl<SysGroupRoleMpMapper, SysGroupRole> implements SysGroupRoleMpService {

}
