package com.kewen.framework.auth.support;

import cn.hutool.core.util.ClassUtil;
import com.kewen.framework.auth.core.AuthConstant;
import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.IAuthObject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kewen
 * @since 2023-12-27
 */

public abstract class AbstractAuthObject implements IAuthObject {

    /**
     * 扫描路径，这里对于AbstractAuthEntity，需要扫描其所有子类以确定flag
     */
    //todo 这个最后改配置
    private static List<String> scanPackages = new ArrayList<String>() {{
        add("com.kewen.framework.auth");
    }};
    private static boolean isInit = false;


    static HashMap<String, Class<? extends AbstractAuthEntity>> authEntityMap = new HashMap<>();

    // TODO: 2023/12/27  init()
    public static void scanPackages(String... scanPackagesInput) {
        if (isInit) {
            return;
        }
        scanPackages.addAll(Arrays.asList(scanPackagesInput));
        Set<Class<?>> classes = new HashSet<>();
        for (String scanPackage : scanPackages) {
            classes.addAll(ClassUtil.scanPackageBySuper(scanPackage, AbstractAuthEntity.class));
        }
        try {
            for (Class<?> aClass : classes) {
                AbstractAuthEntity abstractAuthEntity = (AbstractAuthEntity) aClass.newInstance();
                String flag = abstractAuthEntity.flag();
                if (StringUtils.isEmpty(flag)) {
                    continue;
                }
                authEntityMap.put(flag, abstractAuthEntity.getClass());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("AbstractAuthEntity 子类需要保留无参构造",e);
        }
        isInit = true;
    }

    @SneakyThrows
    public AbstractAuthEntity parse(BaseAuth baseAuth) {
        String[] strings = baseAuth.getAuth().split(AuthConstant.AUTH_SPLIT);
        String flag = strings[0];
        Class<? extends AbstractAuthEntity> aClass = authEntityMap.get(flag);
        AbstractAuthEntity abstractAuthEntity = aClass.newInstance();
        abstractAuthEntity.setPropertiesByAuth(baseAuth);
        return abstractAuthEntity;
    }

    @Override
    public void parse(List<BaseAuth> auths) {
        List<AbstractAuthEntity> abstractAuthEntities = new ArrayList<>();
        for (BaseAuth auth : auths) {
            abstractAuthEntities.add(parse(auth));
        }
        setBaseAuth(abstractAuthEntities);
    }

    protected abstract void setBaseAuth(List<AbstractAuthEntity> abstractAuthEntities);
}
