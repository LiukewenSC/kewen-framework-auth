package com.kewen.framework.auth.rabc.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PageReq {
    @NotNull
    private Long page;
    @NotNull
    private Long size;
    private String name;
}
