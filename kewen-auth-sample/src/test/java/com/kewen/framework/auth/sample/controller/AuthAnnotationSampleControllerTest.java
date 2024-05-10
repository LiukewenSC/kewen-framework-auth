package com.kewen.framework.auth.sample.controller;


import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kewen.framework.auth.sys.mp.entity.SysMenu;
import com.kewen.framework.auth.sys.mp.service.SysMenuMpService;
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
public class AuthAnnotationSampleControllerTest {

    @Autowired
    SysMenuMpService sysMenuMpService;

    private long autoId = 0L;
    @Test
    public void initMenuByJson() throws IOException {
        File file = FileUtil.file("../../../script/sql/menu.json");

        if (!file.exists()){
            throw new RuntimeException("未找到文件");
        }

        SysMenuTemplate o = JSON.parseObject(Files.newInputStream(file.toPath()), SysMenuTemplate.class);


        List<SysMenu> convert = convert(o);


        sysMenuMpService.saveBatch(convert);

        System.out.println(o);

        List<SysMenu> list = sysMenuMpService.list();

        System.out.println(JSONObject.toJSONString(list));


    }
    private List<SysMenu> convert(SysMenuTemplate req){
        ArrayList<SysMenu> list = new ArrayList<>();
        req.setId(++autoId);
        list.add(req);
        List<SysMenuTemplate> children = req.getChildren();
        if (!CollectionUtils.isEmpty(children)){
            for (SysMenuTemplate child : children) {
                child.setParentId(req.getId());
                List<SysMenu> convert = convert(child);
                list.addAll(convert);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        File file = FileUtil.file("../../../script/sql/menu.json");

        System.out.println(file.exists());

        System.out.println(file.getAbsolutePath());


    }
}