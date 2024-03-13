package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysUser;
import com.kewen.framework.auth.sys.mp.mapper.SysUserMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysUserMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
public class SysUserMpServiceImpl extends ServiceImpl<SysUserMpMapper, SysUser> implements SysUserMpService {

}