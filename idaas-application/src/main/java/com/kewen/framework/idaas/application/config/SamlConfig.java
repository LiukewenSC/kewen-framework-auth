package com.kewen.framework.idaas.application.config;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.xmlsec.config.impl.JavaCryptoValidationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.security.Provider;
import java.security.Security;

/**
 * 2025/04/17
 *
 * @author kewen
 * @since 1.0.0
 */
@Configuration
public class SamlConfig implements CommandLineRunner {


    private static final Logger log = LoggerFactory.getLogger(SamlConfig.class);

    @Override
    public void run(String... args) throws Exception {
        JavaCryptoValidationInitializer javaCryptoValidationInitializer =
                new JavaCryptoValidationInitializer();
        try {
            //这个方法应该在OpenSAML初始化之前被调用，
            //来确保当前的JCE环境可以符合要求：AES/CBC/ISO10126Padding
            // 对于XML的加密，JCE需要支持ACE（128/256），并使用ISO10126Padding（填充位）
            javaCryptoValidationInitializer.init();
        } catch (InitializationException e) {
            e.printStackTrace();
        }

        Security.addProvider(new BouncyCastleProvider());

        //打印当前已经被安装的所有JCE的provider
        for (Provider jceProvider : Security.getProviders()) {

            log.info("打印当前已经被安装的所有JCE的provider:" + jceProvider.getInfo());
        }

        try {
            log.info("Initializing");
            //正式初始化ＳＡＭＬ服务
            InitializationService.initialize();
        } catch (InitializationException e) {
            throw new RuntimeException("Initialization failed");
        }
    }
}
