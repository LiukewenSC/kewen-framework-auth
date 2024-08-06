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
 * 
 * </p>
 *
 * @author kewen
 * @since 2024-08-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("meeting_room")
public class MeetingRoom extends Model<MeetingRoom> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会议室名字
     */
    @TableField("name")
    private String name;

    /**
     *  会议室容纳人数
     */
    @TableField("user_count")
    private Integer userCount;

    /**
     * 位置
     */
    @TableField("place")
    private String place;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否有电视屏幕
     */
    @TableField("is_video")
    private Integer isVideo;

    /**
     * 是否有投影仪
     */
    @TableField("is_projector")
    private Integer isProjector;

    /**
     * 是否有座机电话
     */
    @TableField("is_phone")
    private Integer isPhone;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
