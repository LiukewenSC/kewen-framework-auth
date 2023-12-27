package com.kewen.framework.auth.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author kewen
 * @descrpition 类对应的工具
 * @since 2023-11-17
 */
public class ClassUtil {

    /**
     * 解析类上泛型的实际类型
     * @param source 源类型
     * @param target 目标类型是否继承什么类？没有的话填Object也可以
     * @return 解析出的实际类型，如果不存在则返回null
     */
    public static Class parseActualT(Class<?> source, Class<?> target){
        // 获取源类型的接口的类型数组
        Type[] genericInterfaces = source.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            // 获取接口的实际类型参数类型数组
            for (Type typeArgument : ((ParameterizedType) genericInterface).getActualTypeArguments()) {
                // 判断类型参数是否为Class类型
                if (typeArgument instanceof Class) {
                    // 将类型参数转换为Class类型
                    Class classType = (Class) typeArgument;
                    // 判断目标类型是否是classType的子类或接口
                    if (target.isAssignableFrom((classType))) {
                        // 若是则返回classType
                        return classType;
                    }
                }
            }
        }
        // 未找到匹配的实际类型，则返回null
        return null;
    }

}
