package com.kewen.framework.auth.core.menu;

import com.kewen.framework.auth.core.AuthMenu;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * API菜单生成处理器
 *  有MenuApiStore才执行
 * @author kewen
 * @since 2024-08-09
 */
public class MenuApiGeneratorProcessor implements ApplicationContextAware,Runnable {

    private static final Logger log = LoggerFactory.getLogger(MenuApiGeneratorProcessor.class);
    private MenuApiServcie menuApiServcie;
    private Map<RequestMappingInfo, HandlerMethod> handlerMethods;

    private final Map<String, MenuApiEntity> menuApiMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        this.handlerMethods = handlerMapping.getHandlerMethods();
        MenuApiServcie menuApiServcie = applicationContext.getBeanProvider(MenuApiServcie.class).getIfAvailable();
        if (menuApiServcie != null){
            this.menuApiServcie = menuApiServcie;
        }
    }

    @Override
    public void run() {
        //处理对应关系，找到路径信息，在此处处理不会引发系统启动的问题
        if (menuApiServcie ==null){
            log.warn("menuApiStore is null");
            return;
        }
        process();

        //获取数据库中的API
        List<MenuApiEntity> dbs = menuApiServcie.list();
        Set<String> dbPaths = dbs.stream().map(MenuApiEntity::getPath).collect(Collectors.toSet());

        //过滤掉数据库中已经包含的API，只创建不包含的
        List<MenuApiEntity> readySaveApis = menuApiMap.entrySet().stream()
                .filter(entry -> !dbPaths.contains(entry.getKey()))  //在数据库中有的则不计入本次列表
                //根据path排序
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(entry -> {
                    //排序之后用雪花算法生成ID
                    MenuApiEntity apiEntity = entry.getValue();
                    apiEntity.setId(menuApiServcie.generateId());
                    return apiEntity;
                }).collect(Collectors.toList());

        //把数据库的还要加到map中去，后续要使用父path找parentId，而且这里应该允许父级覆盖当前
        for (MenuApiEntity db : dbs) {
            menuApiMap.put(db.getPath(), db);
        }

        //组装上级ID
        List<MenuApiEntity> apiSaves = readySaveApis.stream()
                .peek(apiEntity -> {
                    //如果父path和自己是相同的，则把自己设置为根
                    if (apiEntity.getPath().equals(apiEntity.getParentPath()) || apiEntity.getParentPath() == null) {
                        apiEntity.setParentId(menuApiServcie.getRootParentId());
                    } else {
                        //根据parentPath设置parentId
                        apiEntity.setParentId(menuApiMap.get(apiEntity.getParentPath()).getId());
                    }
                }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(apiSaves)) {
            menuApiServcie.saveBatch(apiSaves);
            log.info("添加的接口有: {}",apiSaves.stream().map(MenuApiEntity::getPath).collect(Collectors.joining(";")));
        } else {
            log.info("没有生成新的API接口");
        }
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
                    menuApiMap.putIfAbsent(controllerPath, MenuApiEntity.of(controllerPath, controllerName));
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
            menuApiMap.putIfAbsent(pattern.trim(), MenuApiEntity.of(pattern, methodName, controllerPath));
        }
    }
}
