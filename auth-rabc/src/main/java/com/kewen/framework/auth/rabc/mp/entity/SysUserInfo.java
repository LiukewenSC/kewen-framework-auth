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
 * 用户信息表
 * </p>
 *
 * @author kewen
 * @since 2024-07-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_info")
public class SysUserInfo extends Model<SysUserInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号
     */
    @TableField("user_id")
    private Long userId;

    /**
     * github账号
     */
    @TableField("github_account")
    private String githubAccount;

    /**
     * gitee账号
     */
    @TableField("gitee_account")
    private String giteeAccount;

    /**
     * 微信账号
     */
    @TableField("wechat_account")
    private String wechatAccount;

    /**
     * 职业
     */
    @TableField("profession")
    private String profession;

    /**
     * 个人简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 个人简历
     */
    @TableField("resume")
    private String resume;

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
