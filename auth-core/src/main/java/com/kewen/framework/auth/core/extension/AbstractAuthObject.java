package com.kewen.framework.auth.core.extension;

import cn.hutool.core.util.ClassUtil;
import com.kewen.framework.auth.core.exception.AuthEntityException;
import com.kewen.framework.auth.core.model.AuthConstant;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.model.IAuthObject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象的权限配置对象，此类主要定义了 BaseAuth 与  IFlagAuthEntity 的转换规则，
 * 子类主要关注定义的权限实体与AbstractAuthEntity之间的转换关系即可
 *
 * @author kewen
 * @since 2023-12-27
 */

public abstract class AbstractAuthObject implements IAuthObject {


    /**
     * 保存的 IAuthEntityProvider 与 权限标记的映射关系
     */
    protected static final Map<String, Class<? extends IFlagAuthEntity>> authEntityMap = new ConcurrentHashMap<>();

    //初始化类的操作，扫描所有的IAuthEntityProvider并保存到 map中
    static {
        try {
            Set<Class<?>> classes = ClassUtil.scanPackageBySuper(null, IFlagAuthEntity.class);
            try {
                for (Class<?> aClass : classes) {
                    if (ClassUtil.isAbstractOrInterface(aClass)) {
                        continue;
                    }
                    IFlagAuthEntity abstractAuthEntity = (IFlagAuthEntity) aClass.newInstance();
                    String flag = abstractAuthEntity.flag();
                    if (StringUtils.isEmpty(flag)) {
                        continue;
                    }
                    authEntityMap.put(flag, abstractAuthEntity.getClass());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new AuthEntityException("AbstractIdNameAuthEntity 子类需要保留无参构造", e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public IFlagAuthEntity parseBaseAuth(BaseAuth baseAuth) {
        String[] strings = baseAuth.getAuth().split(AuthConstant.AUTH_SPLIT);
        String flag = strings[0];
        Class<? extends IFlagAuthEntity> aClass = authEntityMap.get(flag);
        if (aClass == null) {
            throw new AuthEntityException("权限标记错误: "+flag);
        }
        IFlagAuthEntity abstractAuthEntity = null;
        try {
            abstractAuthEntity = aClass.newInstance();
        } catch (Exception e) {
            throw new AuthEntityException("实例化["+aClass.getName()+"]错误",e);
        }
        abstractAuthEntity.setProperties(baseAuth);
        return abstractAuthEntity;
    }

    @Override
    public void setProperties(Collection<BaseAuth> auths) {
        List<IFlagAuthEntity> abstractAuthEntities = new ArrayList<>();
        for (BaseAuth auth : auths) {
            abstractAuthEntities.add(parseBaseAuth(auth));
        }
        setBaseAuth(abstractAuthEntities);
    }

    protected abstract void setBaseAuth(List<IFlagAuthEntity> abstractAuthEntities);
}
