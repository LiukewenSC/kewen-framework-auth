package com.kewen.framework.auth.sample.model;

import com.kewen.framework.auth.core.annotation.data.authedit.IdDataAuthEdit;
import com.kewen.framework.auth.core.annotation.data.edit.IdDataEdit;
import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.model.IdReq;
import com.kewen.framework.auth.sample.mp.entity.MeetingRoom;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MeetingRoomAppointmentReq implements IdDataEdit<Long> {

    @NotNull
    Long id;
    String time;

    @Override
    public Long getDataId() {
        return id;
    }
}
