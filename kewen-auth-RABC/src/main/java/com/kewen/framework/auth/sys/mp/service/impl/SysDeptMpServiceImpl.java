package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysDept;
import com.kewen.framework.auth.sys.mp.mapper.SysDeptMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysDeptMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
public class SysDeptMpServiceImpl extends ServiceImpl<SysDeptMpMapper, SysDept> implements SysDeptMpService {

}
