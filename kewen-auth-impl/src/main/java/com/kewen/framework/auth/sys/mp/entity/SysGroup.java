package com.kewen.framework.auth.sys.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色组表
 * </p>
 *
 * @author kewen
 * @since 2023-04-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_group")
public class SysGroup extends Model<SysGroup> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户组id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户组名
     */
    @TableField("name")
    private String name;

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
