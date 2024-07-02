package com.kewen.framework.auth.web.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.authedit.AuthDataEditAspect;
import com.kewen.framework.auth.core.annotation.data.edit.DataCheckAspect;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeAspect;
import com.kewen.framework.auth.core.annotation.data.range.MybatisDataRangeInterceptor;
import com.kewen.framework.auth.core.annotation.menu.MenuAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 权限核心core模块所需要的配置
 * @author kewen
 * @since 2023-12-28
 */
@Configuration
public class AuthCoreConfig {

    /**
     * 注解处理器
     * 这里需要添加@Lazy注解，否则报循环依赖问题，暂时未找到为啥会循环依赖，而在SpringSecurity中则不会有此问题， 真是神奇啊
     * The dependencies of some of the beans in the application context form a cycle:
     *
     *    authAnnotationSampleController (field com.kewen.framework.auth.sample.mp.service.TestauthAnnotationBusinessMpService com.kewen.framework.auth.sample.controller.AuthAnnotationSampleController.testauthAnnotationBusinessMpService)
     *       ↓
     *    testauthAnnotationBusinessMpServiceImpl (field protected com.baomidou.mybatisplus.core.mapper.BaseMapper com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.baseMapper)
     *       ↓
     *    testauthAnnotationBusinessMpMapper defined in file [C:\Projects\kewen-framework-auth\kewen-auth-sample\target\classes\com\kewen\framework\auth\sample\mp\mapper\TestauthAnnotationBusinessMpMapper.class]
     * ┌─────┐
     * |  com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration
     * ↑     ↓
     * |  com.kewen.framework.auth.web.config.AuthCoreConfig (field private com.kewen.framework.auth.core.annotation.AnnotationAuthHandler com.kewen.framework.auth.web.config.AuthCoreConfig.annotationAuthHandler)
     * ↑     ↓
     * |  com.kewen.framework.boot.auth.config.AuthRabcConfig (field com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite com.kewen.framework.boot.auth.config.AuthRabcConfig.sysAuthMenuComposite)
     * ↑     ↓
     * |  memorySysAuthMenuComposite (field private com.kewen.framework.auth.rabc.mp.service.SysMenuMpService com.kewen.framework.auth.rabc.composite.impl.MemorySysAuthMenuComposite.sysMenuService)
     * ↑     ↓
     * |  sysMenuMpServiceImpl (field protected com.baomidou.mybatisplus.core.mapper.BaseMapper com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.baseMapper)
     * ↑     ↓
     * |  sysMenuMpMapper defined in file [C:\Projects\kewen-framework-auth\kewen-auth-rabc\target\classes\com\kewen\framework\auth\rabc\mp\mapper\SysMenuMpMapper.class]
     * └─────┘
     *
     */
    @Autowired
    @Lazy
    private AnnotationAuthHandler annotationAuthHandler;



    /*--------------------------------------core.annotation.下配置--------------------------------------*/
    /**
     *
     * 数据编辑权限切面处理
     * @return
     */
    @Bean
    AuthDataEditAspect authDataEditAspect() {
        AuthDataEditAspect aspect = new AuthDataEditAspect();
        aspect.setAnnotationAuthAdaptor(annotationAuthHandler);
        return aspect;
    }

    /**
     * 数据校验切面处理
     * @return
     */
    @Bean
    DataCheckAspect dataCheckAspect(){
        DataCheckAspect aspect = new DataCheckAspect();
        aspect.setAnnotationAuthHandler(annotationAuthHandler);
        return aspect;
    }

    /**
     * 数据范围切面处理
     * @return
     */
    @Bean
    DataRangeAspect dataRangeAspect(){
        return new DataRangeAspect();
    }

    /**
     * mybatis拦截器实现
     * @return
     */
    @Bean
    MybatisDataRangeInterceptor mybatisDataRangeInterceptor(){
        MybatisDataRangeInterceptor interceptor = new MybatisDataRangeInterceptor();
        interceptor.setAnnotationAuthHandler(annotationAuthHandler);
        return interceptor;
    }

    /*--------------------------------------core.annotation.menu下配置--------------------------------------*/

    /**
     * 菜单访问控制拦截器
     * @return
     */
    @Bean
    MenuAccessInterceptor menuAccessInterceptor() {
        MenuAccessInterceptor interceptor = new MenuAccessInterceptor();
        interceptor.setAnnotationAuthHandler(annotationAuthHandler);
        return interceptor;
    }



}
