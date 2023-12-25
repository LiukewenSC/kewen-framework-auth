package com.kewen.framework.auth.sample;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AuthSample {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AuthSample.class, args);
    }
}