package com.kewen.framework.auth.support;

import cn.hutool.core.util.ClassUtil;
import com.kewen.framework.auth.core.AuthConstant;
import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.IAuthObject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象的权限配置对象，此类主要定义了 BaseAuth 与  AbstractAuthEntity 的转换规则，
 * 子类主要关注定义的权限实体与AbstractAuthEntity之间的转换关系即可
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
    private static final AtomicBoolean isInit = new AtomicBoolean(false);


    static HashMap<String, Class<? extends AbstractAuthEntity>> authEntityMap = new HashMap<>();

    // TODO: 2023/12/27  init()
    public static void scanPackages(String... scanPackagesInput) {
        if (isInit.getAndSet(true)) {
            return;
        }
        try {
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
        } catch (Exception e) {
            isInit.set(false);
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public AbstractAuthEntity parseBaseAuth(BaseAuth baseAuth) {
        String[] strings = baseAuth.getAuth().split(AuthConstant.AUTH_SPLIT);
        String flag = strings[0];
        Class<? extends AbstractAuthEntity> aClass = authEntityMap.get(flag);
        AbstractAuthEntity abstractAuthEntity = aClass.newInstance();
        abstractAuthEntity.setPropertiesByAuth(baseAuth);
        return abstractAuthEntity;
    }

    @Override
    public void setPropertiesFromBaseAuth(Collection<BaseAuth> auths) {
        List<AbstractAuthEntity> abstractAuthEntities = new ArrayList<>();
        for (BaseAuth auth : auths) {
            abstractAuthEntities.add(parseBaseAuth(auth));
        }
        setBaseAuth(abstractAuthEntities);
    }

    protected abstract void setBaseAuth(List<AbstractAuthEntity> abstractAuthEntities);
}
