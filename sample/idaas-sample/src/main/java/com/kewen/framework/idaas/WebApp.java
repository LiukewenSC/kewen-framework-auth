package com.kewen.framework.idaas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.kewen")
@MapperScan("com.kewen.**.mapper")
public class WebApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(WebApp.class, args);
    }
}