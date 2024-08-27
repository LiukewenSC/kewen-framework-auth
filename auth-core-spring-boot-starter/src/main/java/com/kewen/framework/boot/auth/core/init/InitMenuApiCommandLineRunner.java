package com.kewen.framework.boot.auth.core.init;

import com.kewen.framework.auth.core.annotation.menu.MenuApiGeneratorProcessor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

/**
 * 菜单生成调用入口
 * @author kewen
 * @since 2024-08-09
 */
@Setter
public class InitMenuApiCommandLineRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(InitMenuApiCommandLineRunner.class);
    MenuApiGeneratorProcessor menuApiGeneratorProcessor;
    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(menuApiGeneratorProcessor);
        thread.setName("init menu auth");
        thread.setUncaughtExceptionHandler((t, e) -> log.error("初始化菜单权限脚本异常： {}", e.getMessage(), e));
        thread.start();
    }

}
