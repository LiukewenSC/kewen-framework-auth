package com.kewen.framework.auth.rabc.model;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PageConverter {

    public static <T> Page<T> pageReq2BaomidouPage(PageReq pageReq) {
        return new Page<>(pageReq.getPage(), pageReq.getSize());
    }

    /**
     * 将MybatisPlus的分页返回转换为项目中的
     * @param page
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> baomidouPage2PageResult(Page<T> page) {
        return baomidouPage2PageResult(page, page.getRecords());
    }

    /**
     * 将MybatisPlus的分页 page , size 转换为项目中的，并且转换为 入参类型 T
     * @param page
     * @param data
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> baomidouPage2PageResult(Page page, List<T> data) {
        return PageResult.of(page.getCurrent(),page.getSize(),page.getTotal(),data);
    }

    /**
     * 直接执行Service并且组装返回
     * @param req 请求分页
     * @param service 服务对象
     * @param <T> 数据类型
     * @return
     */
    public static <T> PageResult<T> pageAndConvert(PageReq req, IService<T> service) {
        return pageAndConvert(req, service, new Query<T>());
    }

    /**
     * 直接执行Service并且组装返回 带条件
     * @param req 请求分页
     * @param service 服务对象
     * @param query 查询条件
     * @param <T> 数据类型
     * @return
     */
    public static <T> PageResult<T> pageAndConvert(PageReq req, IService<T> service, Query<T> query) {
        Page<T> baomidoPage = new Page<>(req.getPage(), req.getSize());

        Consumer<Page<T>> consumer = query == null ? null : query.getPageConsumer();
        Wrapper<T> wrapper = query == null ? null : query.getWrapper();
        if (consumer != null) {
            consumer.accept(baomidoPage);
        }
        if (wrapper == null) {
            baomidoPage = service.page(baomidoPage);
        } else {
            baomidoPage = service.page(baomidoPage, wrapper);
        }
        return baomidouPage2PageResult(baomidoPage);
    }

    /**
     * 直接执行Service并且组装返回，带自定义条件和转换规则
     * @param req 请求分页
     * @param service 服务对象
     * @param query 查询条件
     * @param function 转换规则
     * @param <T> 源参数
     * @param <R> 返回参数
     * @return
     */
    public static <T, R> PageResult<R> pageAndConvert(PageReq req, IService<T> service, Query<T> query, Function<List<T>, List<R>> function) {
        PageResult<T> pageResult = pageAndConvert(req, service, query);
        List<T> listT = pageResult.getData();
        List<R> listR = function.apply(listT);
        return PageResult.of(pageResult.getPage(), pageResult.getSize(), pageResult.getTotal(), listR);
    }

    @Data
    @Accessors(chain = true)
    public static class Query<T> {
        Consumer<Page<T>> pageConsumer;
        Wrapper<T> wrapper;
    }
}
