package com.kewen.framework.boot.auth.rabc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *  impl 实现相关
 *  有了子包才有此处配置
 * @author kewen
 * @since 2023-12-28
 */
@Configuration
@MapperScan({
        "com.kewen.framework.auth.rabc.**.mapper"
})
@ComponentScan({
        "com.kewen.framework.auth.rabc.mp.service.impl",
        "com.kewen.framework.auth.rabc.composite",
        "com.kewen.framework.auth.rabc.controller"
})
public class AuthRabcScanConfig {

}
