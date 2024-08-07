package com.kewen.framework.auth.rabc.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.function.Consumer;

public class PageConverter {

    public static <T> Page<T> pageReq2BaomidouPage(PageReq pageReq){
        return new Page<>(pageReq.getPage() ,pageReq.getSize());
    }
    public static <T> PageResult<T> baomidouPage2PageResult(Page<T> page){
        return baomidouPage2PageResult(page, page.getRecords());
    }
    public static <T> PageResult<T> baomidouPage2PageResult(Page page,List<T> data){
        PageResult<T> result = new PageResult<>();
        result.setPage(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setData(data);
        return  result;
    }
    public static <T> PageResult<T> pageAndConvert(PageReq req, IService<T> service){
        return pageAndConvert(req, service, null);
    }
    public static <T> PageResult<T> pageAndConvert(PageReq req, IService<T> service, Consumer<Page<T>> consumer){
        Page<T> baomidoPage = new Page<>(req.getPage() ,req.getSize());
        if (consumer != null){
            consumer.accept(baomidoPage);
        }
        baomidoPage = service.page(baomidoPage);
        return baomidouPage2PageResult(baomidoPage);
    }
}
