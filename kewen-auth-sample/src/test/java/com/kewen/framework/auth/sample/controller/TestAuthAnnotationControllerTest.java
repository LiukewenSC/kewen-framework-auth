package com.kewen.framework.auth.sample.controller;


import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuRoute;
import com.kewen.framework.auth.rabc.mp.service.SysMenuRouteMpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置
 * @author kewen
 * @since 2024-05-10 14:10
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAuthAnnotationControllerTest {

    @Autowired
    SysMenuRouteMpService sysMenuMpService;

    private long autoId = 0L;

    /**
     * 使用json初始化数据库信息，需要使用的时候加上注解即可
     * @throws IOException
     */
    //@Test
    public void initMenuByJson() throws IOException {
        File file = FileUtil.file("../../../script/sql/menu.json");

        if (!file.exists()){
            throw new RuntimeException("未找到文件");
        }

        SysMenuTemplate o = JSON.parseObject(Files.newInputStream(file.toPath()), SysMenuTemplate.class);


        List<SysMenuRoute> convert = convert(o);


        sysMenuMpService.saveBatch(convert);

        System.out.println(o);

        List<SysMenuRoute> list = sysMenuMpService.list();

        System.out.println(JSONObject.toJSONString(list));


    }
    private List<SysMenuRoute> convert(SysMenuTemplate req){
        ArrayList<SysMenuRoute> list = new ArrayList<>();
        req.setId(++autoId);
        list.add(req);
        List<SysMenuTemplate> children = req.getChildren();
        if (!CollectionUtils.isEmpty(children)){
            for (SysMenuTemplate child : children) {
                child.setParentId(req.getId());
                List<SysMenuRoute> convert = convert(child);
                list.addAll(convert);
            }
        }
        return list;
    }
}