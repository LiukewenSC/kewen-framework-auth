package com.kewen.framework.auth.sample.mp.service.impl;

import com.kewen.framework.auth.sample.mp.entity.MeetingRoom;
import com.kewen.framework.auth.sample.mp.mapper.MeetingRoomMpMapper;
import com.kewen.framework.auth.sample.mp.service.MeetingRoomMpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kewen
 * @since 2024-08-06
 */
@Service
public class MeetingRoomMpServiceImpl extends ServiceImpl<MeetingRoomMpMapper, MeetingRoom> implements MeetingRoomMpService {

}
