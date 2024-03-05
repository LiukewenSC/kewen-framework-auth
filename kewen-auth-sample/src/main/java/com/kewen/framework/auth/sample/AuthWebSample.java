package com.kewen.framework.auth.sample;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AuthWebSample {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AuthWebSample.class, args);
    }
}