package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysUserPosition;
import com.kewen.framework.auth.sys.mp.mapper.SysUserPositionMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysUserPositionMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户岗位关联表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
@Primary
public class SysUserPositionMpServiceImpl extends ServiceImpl<SysUserPositionMpMapper, SysUserPosition> implements SysUserPositionMpService {

}
