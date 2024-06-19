package com.kewen.framework.auth.sample.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PageReq {
    @NotNull
    private Integer page;
    @NotNull
    private Integer size;
    private String name;
}
