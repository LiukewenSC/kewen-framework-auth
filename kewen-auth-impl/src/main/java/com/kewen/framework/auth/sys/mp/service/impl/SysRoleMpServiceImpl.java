package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysRole;
import com.kewen.framework.auth.sys.mp.mapper.SysRoleMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysRoleMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
public class SysRoleMpServiceImpl extends ServiceImpl<SysRoleMpMapper, SysRole> implements SysRoleMpService {

}
