package com.kewen.framework.auth.sample.model;

import com.kewen.framework.auth.core.annotation.data.authedit.IdDataAuthEdit;
import com.kewen.framework.auth.core.model.IAuthObject;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.sample.mp.entity.MeetingRoom;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MeetingRoomAddReq extends MeetingRoom implements IdDataAuthEdit<Long> {

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
