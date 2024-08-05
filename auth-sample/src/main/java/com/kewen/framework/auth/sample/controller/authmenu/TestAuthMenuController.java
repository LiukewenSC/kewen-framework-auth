package com.kewen.framework.auth.sample.controller.authmenu;

import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testAuthMenuClassMethodController")
@AuthMenu(name = "方法和类上加AuthMenu注解")
public class TestAuthMenuController {

    @GetMapping("/hello")
    @AuthMenu(name = "你好1")
    public String hello(String name) {
        return "hello " + name;
    }

    @GetMapping("/hello2")
    @AuthMenu(name = "你好2")
    public String hello2() {
        return "hello2 ";
    }

    @GetMapping("/hello3")
    @AuthMenu(name = "你好3")
    public String hello3() {
        return "hello3";
    }
}
