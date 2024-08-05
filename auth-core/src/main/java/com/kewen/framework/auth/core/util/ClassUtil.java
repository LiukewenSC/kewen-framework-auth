package com.kewen.framework.auth.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类对应的工具
 * @author kewen
 * @since 2023-11-17 9:01
 */
public class ClassUtil {

    /**
     * 解析类上实现接口泛型的实际类型
     * @param source 源类型
     * @param target 目标类型
     * @return 解析出的实际类型，如果不存在则返回null
     * @deprecated 用 parseInterfaceActualT 代替
     */
    @Deprecated
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

    /**
     * 解析类上泛型的实际类型
     * @param source 需要解析的类
     * @param tSuperClass 期望的目标类型的超类，即<T extend ?> 中extend后的类  ， 没有的话填Object也可以
     * @return 解析出的实际类型，如果不存在则返回null
     */
    public static Class parseInterfaceActualT(Class<?> source, Class<?> tSuperClass){
        // 获取源类型的接口的类型数组
        Type[] genericInterfaces = source.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            Class classType = parseActual( (ParameterizedType) genericInterface,tSuperClass);
            if (classType != null) return classType;
        }
        // 未找到匹配的实际类型，则返回null
        return null;
    }

    /**
     *
     * @param source 需要解析的类
     * @param tSuperClass 期望的目标类型的超类，即<T extend ?> 中extend后的类  ， 没有的话填Object也可以
     * @return
     */
    public static Class parseSuperActualT(Class<?> source, Class<?> tSuperClass){
        Type superclass = source.getGenericSuperclass();
        return parseActual( (ParameterizedType) superclass,tSuperClass);
    }

    /**
     * 解析接口的实际类型
     * @param tSuperClass 期望的目标类型的超类，即<T extend ?> 中extend后的类  ， 没有的话填Object也可以
     * @param parameterizedType
     * @return
     */
    private static Class parseActual( ParameterizedType parameterizedType,Class<?> tSuperClass) {
        // 获取接口的实际类型参数类型数组
        for (Type typeArgument : parameterizedType.getActualTypeArguments()) {
            // 判断类型参数是否为Class类型
            if (typeArgument instanceof Class) {
                // 将类型参数转换为Class类型
                Class classType = (Class) typeArgument;
                // 判断目标类型是否是classType的子类或接口
                if (tSuperClass.isAssignableFrom((classType))) {
                    // 若是则返回classType
                    return classType;
                }
            }
        }
        return null;
    }



}
