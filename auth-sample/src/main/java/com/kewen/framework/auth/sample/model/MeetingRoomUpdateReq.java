package com.kewen.framework.auth.sample.model;

import com.kewen.framework.auth.core.annotation.data.authedit.IdDataAuthEdit;
import com.kewen.framework.auth.core.annotation.data.edit.IdDataEdit;
import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.sample.mp.entity.MeetingRoom;

public class MeetingRoomUpdateReq extends MeetingRoom implements IdDataEdit<Long>,IdDataAuthEdit<Long> {

    SimpleAuthObject authObject;

    @Override
    public Long getDataId() {
        return getId();
    }

    @Override
    public IAuthObject getAuthObject() {
        return authObject;
    }
}
