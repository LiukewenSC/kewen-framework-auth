package com.kewen.framework.auth.rabc.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private Long page;
    private Long size;
    private Long total;
    private List<T> data;
}
