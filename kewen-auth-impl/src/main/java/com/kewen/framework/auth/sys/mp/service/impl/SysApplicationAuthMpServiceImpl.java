package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysApplicationAuth;
import com.kewen.framework.auth.sys.mp.mapper.SysApplicationAuthMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysApplicationAuthMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用权限表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
public class SysApplicationAuthMpServiceImpl extends ServiceImpl<SysApplicationAuthMpMapper, SysApplicationAuth> implements SysApplicationAuthMpService {

}
