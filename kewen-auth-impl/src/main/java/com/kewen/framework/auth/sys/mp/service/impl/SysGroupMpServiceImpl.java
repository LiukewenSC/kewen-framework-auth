package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysGroup;
import com.kewen.framework.auth.sys.mp.mapper.SysGroupMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysGroupMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色组表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
public class SysGroupMpServiceImpl extends ServiceImpl<SysGroupMpMapper, SysGroup> implements SysGroupMpService {

}
