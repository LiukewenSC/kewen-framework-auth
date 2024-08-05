package com.kewen.framework.boot.auth.config;

import com.kewen.framework.auth.rabc.RabcAnnotationAuthHandler;
import com.kewen.framework.boot.auth.properties.DataRangeDatabaseFieldProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(DataRangeDatabaseFieldProperties.class)
public class AuthRabcScanConfig {

}
