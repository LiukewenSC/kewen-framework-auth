package com.kewen.framework.auth.sample.controller.security;

import com.kewen.framework.auth.rabc.model.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class TestSecurityController {

    @PreAuthorize("hasAuthority(ROLE_1)")
    @GetMapping("/admin")
    public Result admin(){
        return Result.success("hasAuthority(ROLE_1)");
    }

}
