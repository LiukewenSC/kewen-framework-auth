package com.kewen.framework.auth.rabc.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class IdsReq {
    @NotNull
    private List<Long> ids;
}
