package com.kewen.framework.idaas.model;


import lombok.Data;

import java.util.List;


/**
 * @author kewen
 * @descrpition 分页返回
 */
@Data
public class PageResult<T> {


    private Integer page;
    private Integer size;
    private Integer total;
    private List<T> data;

    public static <T> PageResult<T> of(Integer page, Integer size, Integer total, List<T> data){
        PageResult<T> result = new PageResult<>();
        result.setPage(page);
        result.setSize(size);
        result.setTotal(total);
        result.setData(data);
        return result;
    }
}
