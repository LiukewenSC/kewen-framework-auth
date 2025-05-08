package com.kewen.framework.auth.rabc.mp.service.impl;

import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.rabc.mp.mapper.SysUserMpMapper;
import com.kewen.framework.auth.rabc.mp.service.SysUserMpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2024-07-29
 */
@Service
public class SysUserMpServiceImpl extends ServiceImpl<SysUserMpMapper, SysUser> implements SysUserMpService {

}
