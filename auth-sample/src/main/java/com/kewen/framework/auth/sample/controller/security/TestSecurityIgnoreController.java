package com.kewen.framework.auth.sample.controller.security;

import com.kewen.framework.auth.rabc.model.Result;
import com.kewen.framework.auth.security.annotation.SecurityIgnore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityIgnore
@RequestMapping("/hello")
public class TestSecurityIgnoreController {

    @RequestMapping("/hello")
    public Result hello(){
        return Result.success("SecurityIgnore method");
    }

}
