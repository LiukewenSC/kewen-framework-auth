package com.kewen.framework.auth.sample.mp.entity;

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
 * 用户部门关联表
 * </p>
 *
 * @author kewen
 * @since 2023-12-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_dept")
public class SysUserDept extends Model<SysUserDept> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 部门id
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 是否主要归属部门 0-非主要部门 1-主要部门
     */
    @TableField("is_primary")
    private Integer isPrimary;

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
