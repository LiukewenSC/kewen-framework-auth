package com.kewen.framework.idaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.kewen")
public class WebApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(WebApp.class, args);
    }
}