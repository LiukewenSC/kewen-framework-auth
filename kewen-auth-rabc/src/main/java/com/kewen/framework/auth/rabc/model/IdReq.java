package com.kewen.framework.auth.rabc.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IdReq {
    @NotNull
    private Long id;
}
