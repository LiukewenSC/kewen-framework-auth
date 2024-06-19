package com.kewen.framework.auth.sample.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private int page;
    private int size;
    private int total;
    private List<T> data;
}
