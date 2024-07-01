package com.kewen.framework.auth.rabc.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kewen.framework.auth.rabc.mp.entity.SysUserCredential;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户凭证表 Mapper 接口
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Mapper
public interface SysUserCredentialMpMapper extends BaseMapper<SysUserCredential> {

}
