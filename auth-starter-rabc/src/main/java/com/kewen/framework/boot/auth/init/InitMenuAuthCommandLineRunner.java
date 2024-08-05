package com.kewen.framework.boot.auth.init;

import cn.hutool.core.util.IdUtil;
import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import com.kewen.framework.auth.rabc.model.MenuTypeConstant;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuApi;
import com.kewen.framework.auth.rabc.mp.service.SysMenuApiMpService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InitMenuAuthCommandLineRunner implements CommandLineRunner, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(InitMenuAuthCommandLineRunner.class);
    private SysMenuApiMpService sysMenuPathMpService;
    private Map<RequestMappingInfo, HandlerMethod> handlerMethods;

    private final Map<String, ApiEntity> menuApiMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.sysMenuPathMpService = applicationContext.getBean(SysMenuApiMpService.class);
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        this.handlerMethods = handlerMapping.getHandlerMethods();
    }

    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(() -> {

            //处理对应关系，找到路径信息，在此处处理不会引发系统启动的问题
            process();

            List<SysMenuApi> dbs = sysMenuPathMpService.list();
            Set<String> dbPaths = dbs.stream().map(SysMenuApi::getPath).collect(Collectors.toSet());

            List<ApiEntity> apiEntities = menuApiMap.entrySet().stream()
                    .filter(entry -> !dbPaths.contains(entry.getKey()))  //在数据库中有的则不计入本次列表
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(entry -> {
                        ApiEntity apiEntity = entry.getValue();
                        apiEntity.setId(IdUtil.getSnowflakeNextId());
                        return apiEntity;
                    }).collect(Collectors.toList());

            //把数据库的还要加到map中去，后续要使用父path找parentId，而且这里应该允许父级覆盖当前
            for (SysMenuApi db : dbs) {
                menuApiMap.put(
                        db.getPath(),
                        new ApiEntity().setId(db.getId()).setName(db.getName()).setPath(db.getPath()).setParentId(db.getParentId())
                );
            }

            //组装上级ID并转换
            List<SysMenuApi> requestList = apiEntities.stream()
                    .peek(apiEntity -> {
                        //如果父path和自己是相同的，则把自己设置为根
                        if (apiEntity.getPath().equals(apiEntity.getParentPath()) || apiEntity.getParentPath() == null) {
                            apiEntity.setParentId(0L);
                        } else {
                            //根据parentPath设置parentId
                            apiEntity.setParentId(menuApiMap.get(apiEntity.getParentPath()).getId());
                        }
                    }).map(apiEntity -> new SysMenuApi()
                            .setId(apiEntity.getId())
                            .setName(apiEntity.getName())
                            .setPath(apiEntity.getPath())
                            .setParentId(apiEntity.getParentId())
                            .setAuthType(apiEntity.getParentId() == 0L ? MenuTypeConstant.AUTH_TYPE.OWNER : MenuTypeConstant.AUTH_TYPE.PARENT)
                    ).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(requestList)) {
                sysMenuPathMpService.saveBatch(requestList);
                log.info("添加的接口有: {}",requestList.stream().map(SysMenuApi::getPath).collect(Collectors.joining(";")));
            } else {
                log.info("没有生成新的API接口");
            }
        });
        thread.setName("init menu auth");
        thread.setUncaughtExceptionHandler((t, e) -> log.error("初始化菜单权限脚本异常： {}", e.getMessage(), e));
        thread.start();
    }

    /**
     * 处理AuthMenu注解相关的path和Controller的关系
     */
    private void process() {
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            /*
                path始终以RequestmappingHandlerMapping为准
                name 通过把 controller 注解AuthMenu#name 与method注解AuthMenu#name 合并生成
                类还需要单独生成一个请求，以创建上下层级关系
             */
            String controllerName;
            String controllerPath;

            RequestMappingInfo mappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            AuthMenu authMenu;
            boolean hasAuthMenu = false;
            boolean hasAddClassVirtualPath = false;


            //处理类上的注解
            Class<?> controllerClass = handlerMethod.getMethod().getDeclaringClass();
            if ((authMenu = controllerClass.getAnnotation(AuthMenu.class)) != null) {
                //类上有@AuthMenu注解
                controllerName = authMenu.name();
                hasAuthMenu = true;
            } else {
                //类上没有@AuthMenu注解，就直接生成一个以controller的类建立上下级关系
                controllerName =controllerClass.getSimpleName();
            }

            //获取controller的 路径，如果都没有标注RequestMapping或者没有路径，那就算了，不做上下级的虚拟路径了
            RequestMapping annRequestMapping = controllerClass.getAnnotation(RequestMapping.class);
            if (annRequestMapping != null && annRequestMapping.value().length !=0) {
                //创建class的路径，这个路径基本上是虚拟的，只加入一次，但是可以被基于方法的覆盖
                controllerPath = annRequestMapping.value()[0];
                //这里如果Controller只有@RequestMapping，但是没有加路径或者为/根路径的话，也不添加做虚拟路径
                if (StringUtils.isNotBlank(controllerPath) && !controllerPath.equals("/")) {
                    hasAddClassVirtualPath = true;
                }
            } else {
                controllerPath = null;
            }

            String methodName = null;
            //处理方法上的注解，方法注解可能方法上有AuthMenu注解，也可能只有Controller上有注解
            if (handlerMethod.hasMethodAnnotation(AuthMenu.class)) {
                authMenu = handlerMethod.getMethodAnnotation(AuthMenu.class);
                //没有名字以方法名为准
                methodName = controllerName + ">" + authMenu.name();
                hasAuthMenu = true;
            } else if (hasAuthMenu) {
                //类上有注解，但是方法上没有注解
                methodName = controllerName + ">" + handlerMethod.getMethod().getName();
            }

            //判断有菜单API注解才执行
            if (hasAuthMenu) {
                //有权限注解，而且需要添加controller虚拟路径才添加
                if (hasAddClassVirtualPath){
                    menuApiMap.putIfAbsent(controllerPath, ApiEntity.of(controllerPath, controllerName));
                }
                putMenuAuth(mappingInfo, methodName, controllerPath);
            }
        }
    }


    /**
     * @param mappingInfo controller生成的信息
     * @param methodName  名称，不会为空
     */
    private void putMenuAuth(RequestMappingInfo mappingInfo, String methodName, String controllerPath) {
        PatternsRequestCondition patternsCondition = mappingInfo.getPatternsCondition();
        Set<String> patterns = patternsCondition.getPatterns();
        for (String pattern : patterns) {
            menuApiMap.putIfAbsent(pattern.trim(), ApiEntity.of(pattern, methodName, controllerPath));
        }
    }

    @Data
    @Accessors(chain = true)
    public static class ApiEntity {
        Long id;
        String path;
        String name;
        Long parentId;
        String parentPath;

        public static ApiEntity of(String path, String name) {
            return of(path, name, null);
        }

        public static ApiEntity of(String path, String name, String parentPath) {
            ApiEntity apiEntity = new ApiEntity();
            apiEntity.setPath(path);
            apiEntity.setName(name);
            apiEntity.setParentPath(parentPath);
            return apiEntity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ApiEntity apiEntity = (ApiEntity) o;
            return Objects.equals(path, apiEntity.path);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(path);
        }
    }
}
