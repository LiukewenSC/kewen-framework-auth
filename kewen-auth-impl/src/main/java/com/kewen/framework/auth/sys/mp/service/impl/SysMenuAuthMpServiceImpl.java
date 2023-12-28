package com.kewen.framework.auth.sys.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kewen.framework.auth.sys.mp.entity.SysMenuAuth;
import com.kewen.framework.auth.sys.mp.mapper.SysMenuAuthMpMapper;
import com.kewen.framework.auth.sys.mp.service.SysMenuAuthMpService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Service
@Primary
public class SysMenuAuthMpServiceImpl extends ServiceImpl<SysMenuAuthMpMapper, SysMenuAuth> implements SysMenuAuthMpService {

}
