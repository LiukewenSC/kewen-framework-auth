package com.kewen.framework.boot.auth.init;

import com.kewen.framework.auth.core.annotation.menu.AuthMenu;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuRequest;
import com.kewen.framework.auth.rabc.mp.service.SysMenuRequestMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Collectors;

public class InitMenuAuthCommandLineRunner implements CommandLineRunner, ApplicationContextAware {

    @Autowired
    SysMenuRequestMpService sysMenuRequestMpService;

    HashMap<String, String> pathAuthMenu = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        HashSet<SysMenuRequest> hashSet = new HashSet<>();
        for (Map.Entry<String, String> entry : pathAuthMenu.entrySet()) {
            hashSet.add(new SysMenuRequest().setPath(entry.getKey()).setName(entry.getValue()));
        }
        List<SysMenuRequest> list = sysMenuRequestMpService.list();

        List<SysMenuRequest> requestList = hashSet.stream()
                .filter(a -> list.stream().noneMatch(b -> a.getPath().equals(b.getPath())))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(requestList)){
            sysMenuRequestMpService.saveBatch(requestList);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            AuthMenu authMenu;
            if (handlerMethod.hasMethodAnnotation(AuthMenu.class)) {
                authMenu = handlerMethod.getMethodAnnotation(AuthMenu.class);
                putMenuAuth(mappingInfo,authMenu);
            // 处理Controller上添加AuthMenu的，表示这些接口全部都要验证
            } else if ((authMenu = handlerMethod.getMethod().getDeclaringClass().getAnnotation(AuthMenu.class)) != null) {
                putMenuAuth(mappingInfo,authMenu);
            }
        }

    }
    private void putMenuAuth(RequestMappingInfo mappingInfo,AuthMenu authMenu) {
        PatternsRequestCondition patternsCondition = mappingInfo.getPatternsCondition();
        Set<String> patterns = patternsCondition.getPatterns();
        for (String pattern : patterns) {
            String path = StringUtils.isNotBlank(authMenu.path())?authMenu.path():pattern.trim();
            String name = authMenu.name();
            pathAuthMenu.putIfAbsent(path, name);
        }
    }
}
