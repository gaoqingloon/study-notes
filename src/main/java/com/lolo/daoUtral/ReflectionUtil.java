package com.lolo.daoUtral;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtil {//反射的工具类

    /**
     * 获取形参clazz的父类的泛型
     */
    public static Class getGenericParam(Class clazz) {//Class clazz = CustomerDAO.class

        Type type = clazz.getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        Type[] typeArguments = paramType.getActualTypeArguments();//返回泛型参数构成的数组
        return (Class) typeArguments[0];
    }
}
