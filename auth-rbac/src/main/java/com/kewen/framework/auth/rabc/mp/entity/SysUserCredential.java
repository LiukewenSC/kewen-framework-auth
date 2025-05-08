package com.kewen.framework.auth.rabc.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户凭证表
 * </p>
 *
 * @author kewen
 * @since 2024-07-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_credential")
public class SysUserCredential extends Model<SysUserCredential> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户表的id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户密码,可以为空，为拓展免密登录做准备
     */
    @TableField("password")
    private String password;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 凭证过期时间 每次修改密码应修改过期时间 ， 为空表示系统无过期时间设定
     */
    @TableField("password_expired_time")
    private LocalDateTime passwordExpiredTime;

    /**
     * 账号锁定截止时间，为空或早于当前时间则为不锁定
     */
    @TableField("account_locked_deadline")
    private LocalDateTime accountLockedDeadline;

    /**
     * 账号是否启用
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
