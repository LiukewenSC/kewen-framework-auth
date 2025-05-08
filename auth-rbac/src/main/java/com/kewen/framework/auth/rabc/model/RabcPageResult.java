package com.kewen.framework.auth.rabc.model;

import lombok.Data;

import java.util.List;

@Data
public class RabcPageResult<T> {

    private Long page;
    private Long size;
    private Long total;
    private List<T> data;

    public static <T> RabcPageResult<T> of(Long page, Long size, Long total, List<T> data){
        RabcPageResult<T> result = new RabcPageResult<>();
        result.setPage(page);
        result.setSize(size);
        result.setTotal(total);
        result.setData(data);
        return result;
    }
}
