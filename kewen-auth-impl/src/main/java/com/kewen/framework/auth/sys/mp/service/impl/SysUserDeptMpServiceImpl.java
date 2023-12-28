package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysUserDept;
import com.kewen.framework.auth.sys.mp.mapper.SysUserDeptMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysUserDeptMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户部门关联表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
@Primary
public class SysUserDeptMpServiceImpl extends ServiceImpl<SysUserDeptMpMapper, SysUserDept> implements SysUserDeptMpService {

}
