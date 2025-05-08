package com.kewen.framework.auth.rabc.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RabcIdReq {
    @NotNull
    private Long id;
}
