package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysUserCredential;
import com.kewen.framework.auth.sys.mp.mapper.SysUserCredentialMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysUserCredentialMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户凭证表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
@Primary
public class SysUserCredentialMpServiceImpl extends ServiceImpl<SysUserCredentialMpMapper, SysUserCredential> implements SysUserCredentialMpService {

}
