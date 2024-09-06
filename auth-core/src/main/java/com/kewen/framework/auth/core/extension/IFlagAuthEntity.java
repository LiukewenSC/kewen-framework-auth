package com.kewen.framework.auth.core.extension;

import cn.hutool.core.util.StrUtil;
import com.kewen.framework.auth.core.exception.AuthEntityException;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.model.IAuthEntity;

import com.kewen.framework.auth.core.model.AuthConstant;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 简单的权限实体接口，用以约定权限体的 ID、name属性值以及权限标记类型、设置权限值
 * @author kewen
 * @since 2023-12-28
 */
public interface IFlagAuthEntity<ID> extends IAuthEntity {

    /**
     * 获取权限标记类型
     * 如： USER  ROLE  DEPT
     * @return
     */
    String flag();

    /**
     * 根据基础权限设置值
     * @param baseAuth
     */
    default void setProperties(BaseAuth baseAuth){
        String auth = baseAuth.getAuth();
        String description = baseAuth.getDescription();
        String[] authSplit = auth.split(AuthConstant.AUTH_SPLIT);
        String[] descSplit = description.split(AuthConstant.AUTH_SPLIT);
        String flag = authSplit[0];
        if (!flag().equals(flag)){
            throw new AuthEntityException("解析"+flag()+"出错");
        }
        String[] idSplit = authSplit[1].split(AuthConstant.AUTH_SUB_SPLIT);
        String[] nameSplit = descSplit[1].split(AuthConstant.AUTH_SUB_SPLIT);
        //循环权限字符串，设置值
        try {
            for (int i = 0; i < idSplit.length; i++) {
                deSerialize(i,idSplit[i],nameSplit[i]);
            }
        } catch (Exception e) {
            throw new AuthEntityException("解析BaseAuth异常"+baseAuth);
        }
    }

    /**
     * 根据拆分的字符串设置每一个位置的值，拆分为对象
     * 如 权限字符串Auth为：DEPT_ROLE__1_1，Description为：DEPT_ROLE__根部门_超级管理员
     * 这里第一次需要处理的就是DEPT对应的数据 即->  0 1 根部门
     * 第二次需要处理的就是ROLE对应的数据 即->  1 1 超级管理员
     * @param pos 位置，从0开始
     * @param posAuth auth 对应的字符串
     * @param posDescription desc对应的字符串
     */
    void deSerialize(int pos, String posAuth, String posDescription);

    @Override
    default BaseAuth getAuth() {
        String flag = flag();
        //获取到由多少个对象拼接的
        int totalPosition = flag().split(AuthConstant.AUTH_SUB_SPLIT).length;
        //组装标记
        StringBuilder authBuilder = new StringBuilder(flag);
        StringBuilder descriptionBuilder = new StringBuilder(flag);
        //组装主分割
        authBuilder.append(AuthConstant.AUTH_SPLIT);
        descriptionBuilder.append(AuthConstant.AUTH_SPLIT);

        //组装值
        for (int i = 0; i < totalPosition; i++) {
            Pair<String, String> encode = serialize(i);
            String subId = encode.getLeft();
            authBuilder.append(subId).append(AuthConstant.AUTH_SUB_SPLIT);
            String subDescription = encode.getRight();
            descriptionBuilder.append(subDescription).append(AuthConstant.AUTH_SUB_SPLIT);
        }
        String auth = StrUtil.removeSuffix(authBuilder.toString(), AuthConstant.AUTH_SUB_SPLIT);
        String description = StrUtil.removeSuffix(descriptionBuilder.toString(), AuthConstant.AUTH_SUB_SPLIT);
        return new BaseAuth(auth,description);
    }

    /**
     * 根据位置生成对应的字符串
     * @param pos
     * @return
     */
    Pair<String,String> serialize(int pos);
}
