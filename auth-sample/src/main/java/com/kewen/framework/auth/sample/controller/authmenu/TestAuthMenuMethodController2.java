package com.kewen.framework.auth.sample.controller.authmenu;


import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestAuthMenuMethodController2 {


    @GetMapping("/testAuthMenuMethod2")
    @AuthMenu(name = "测试只在Method上加@AuthMenu权限注解2，Controller上没有注解")
    public Object testDataRange() {
        return "";
    }
}
