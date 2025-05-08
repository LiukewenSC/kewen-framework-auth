package com.kewen.framework.idaas.web;



import com.kewen.framework.idaas.authentication.context.AuthenticationContext;
import com.kewen.framework.idaas.authentication.context.AuthenticationContextHolder;
import com.kewen.framework.idaas.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2025/04/13
 *
 * @author kewen
 * @since 1.0.0
 */
@RestController
public class HelloController {


    @GetMapping("/public/hello")
    public Result<String> publicHello() {
        return Result.success("hello");
    }

    @GetMapping("/hello")
    public Result hello() {
        AuthenticationContext userContext = AuthenticationContextHolder.getUserContext();
        return Result.success(userContext);
    }
}
