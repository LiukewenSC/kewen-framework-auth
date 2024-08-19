package com.kewen.framework.auth.rabc.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private Long page;
    private Long size;
    private Long total;
    private List<T> data;

    public static <T> PageResult<T> of(Long page, Long size, Long total, List<T> data){
        PageResult<T> result = new PageResult<>();
        result.setPage(page);
        result.setSize(size);
        result.setTotal(total);
        result.setData(data);
        return result;
    }
}
