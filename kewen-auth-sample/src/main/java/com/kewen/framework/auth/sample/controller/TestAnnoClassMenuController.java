package com.kewen.framework.auth.sample.controller;

import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/TestAnnoClassMenuController")
@AuthMenu(name = "测试Class菜单权限注解")
public class TestAnnoClassMenuController {

    @GetMapping("/hello")
    public String hello(String name) {
        return "hello " + name;
    }
    @GetMapping("/hello2")
    public String hello2() {
        return "hello2 ";
    }

}
