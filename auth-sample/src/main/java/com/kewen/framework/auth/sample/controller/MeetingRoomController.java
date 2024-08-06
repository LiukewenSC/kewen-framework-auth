package com.kewen.framework.auth.sample.controller;

import com.kewen.framework.auth.core.annotation.AuthDataService;
import com.kewen.framework.auth.core.annotation.data.AuthDataAuthEdit;
import com.kewen.framework.auth.core.annotation.data.AuthDataOperation;
import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import com.kewen.framework.auth.rabc.model.*;
import com.kewen.framework.auth.sample.model.MeetingRoomAddReq;
import com.kewen.framework.auth.sample.model.MeetingRoomAppointmentReq;
import com.kewen.framework.auth.sample.model.MeetingRoomUpdateReq;
import com.kewen.framework.auth.sample.mp.service.MeetingRoomMpService;
import com.kewen.framework.auth.sample.mp.entity.MeetingRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.annotation.Validated;


/**
 *  接口
 *
 * @author kewen
 * @since 2024-08-06
 */
@RestController
@RequestMapping("/meetingRoom")
public class MeetingRoomController {

    private static final Logger log = LoggerFactory.getLogger(MeetingRoomController.class);
    @Autowired
    MeetingRoomMpService meetingRoomService;

    @Autowired
    AuthDataService authDataService;
    /**
     * 创建会议室
     * @param entity
     * @return
     */
    @PostMapping("/add")
    @AuthMenu(name = "创建会议室")
    @AuthDataAuthEdit(businessFunction = "meeting_room",operate = "edit_info",before = false)
    @Transactional(rollbackFor = Exception.class)
    public Result add(@RequestBody MeetingRoomAddReq entity){
        meetingRoomService.save(entity);
        return Result.success();
    }
    /**
     * 创建会议室,第二种写法，不依赖于注解
     * @param entity
     * @return
     */
    @PostMapping("/add2")
    @AuthMenu(name = "创建会议室")
    @Transactional(rollbackFor = Exception.class)
    public Result add2(@RequestBody MeetingRoomAddReq entity){
        meetingRoomService.save(entity);
        authDataService.editDataAuths("meeting_room",entity.getDataId(),"edit_info",entity.getAuthObject());
        return Result.success();
    }
    /**
     * 删除会议室
     * @param req
     * @return
     */
    @PostMapping(value = "/deleteById")
    @AuthMenu(name = "删除会议室")
    public Result deleteById(@RequestBody @Validated IdReq req ) {
        meetingRoomService.removeById(req.getId());
        return Result.success();
    }

    /**
     * 编辑会议室方式2，
     * 不依赖@AuthDataAuthEdit
     * @param entity
     * @return
     */
    @PostMapping("/updateById")
    @Transactional(rollbackFor = Exception.class)
    @AuthDataOperation(businessFunction = "meeting_room",operate = "edit_info")
    public Result updateById(@RequestBody MeetingRoomUpdateReq entity){
        meetingRoomService.updateById(entity);
        authDataService.editDataAuths("meeting_room",entity.getDataId(),"appointment",entity.getAuthObject());
        return Result.success();
    }
    /**
     * 编辑会议室
     * @param entity
     * @return
     */
    @PostMapping("/updateById2")
    @Transactional(rollbackFor = Exception.class)
    @AuthDataOperation(businessFunction = "meeting_room",operate = "edit_info")
    @AuthDataAuthEdit(businessFunction = "meeting_room",operate = "appointment")
    public Result updateById2(@RequestBody MeetingRoomUpdateReq entity){

        meetingRoomService.updateById(entity);

        return Result.success();
    }

    /**
     * 查询可编辑列表
     * @param pageReq
     * @return
     */
    @GetMapping(value = "/page")
    @AuthDataOperation(businessFunction = "meeting_room",operate = "edit_info")
    public Result<PageResult<MeetingRoom>> pageQuery(@Validated PageReq pageReq) {
        PageResult<MeetingRoom> meetingRoomPageResult = PageConverter.pageAndConvert(pageReq, meetingRoomService);
        return Result.success(meetingRoomPageResult);
    }

    /**
     * 查询可预约列表
     * @param pageReq
     * @return
     */
    @GetMapping(value = "/pageAppointments")
    @AuthDataOperation(businessFunction = "meeting_room",operate = "appointment")
    public Result<PageResult<MeetingRoom>> pageAppointments(@Validated PageReq pageReq) {
        PageResult<MeetingRoom> meetingRoomPageResult = PageConverter.pageAndConvert(pageReq, meetingRoomService);
        return Result.success(meetingRoomPageResult);
    }

    /**
     * 预约会议室
     * @param req 会议室ID，其实还有其他的入参，比如时间段等，这里暂时不管
     * @return
     */
    @PostMapping(value = "/appointmentMeetingRoom")
    @AuthDataOperation(businessFunction = "meeting_room",operate = "appointment")
    public Result appointmentMeetingRoom(@RequestBody @Validated MeetingRoomAppointmentReq req) {
        String time = req.getTime();
        //预约会议室的逻辑
        log.info("预约成功{}", time);
        return Result.success(time);
    }

}
