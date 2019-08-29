package com.tuacy.tableshard.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @name: ReflectUtil
 * @author: tuacy.
 * @date: 2019/7/17.
 * @version: 1.0
 * @Description: 反射工具类
 */
public class ReflectUtil {

    private ReflectUtil() {
    }

    /**
     * 对象拷贝
     */
    public static String getFieldValue(Object source, String fieldName) {
        for (Class<?> clazz = source.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] srcFields = clazz.getDeclaredFields();
                for (Field srcField : srcFields) {
                    try {
                        if (Modifier.isStatic(srcField.getModifiers())) {
                            // 过滤掉static
                            continue;
                        }
                        srcField.setAccessible(true);
                        if (srcField.getName().equals(fieldName)) {
                            return srcField.get(source).toString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;

    }

    /**
     * 对象拷贝
     */
    public static void copyFieldToBean(Object from, Object to) {
        Map<String, Object> srcMap = new HashMap<>();
        // from 子类已经父类里面所有的属性
        for (Class<?> clazz = from.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] srcFields = clazz.getDeclaredFields();
                for (Field srcField : srcFields) {
                    try {
                        if (Modifier.isStatic(srcField.getModifiers())) {
                            // 过滤掉static
                            continue;
                        }
                        srcField.setAccessible(true);
                        srcMap.put(srcField.getName(), srcField.get(from)); //获取属性值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        // to 子类已经父类里面所有的属性
        for (Class<?> clazz = to.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] destFields = clazz.getDeclaredFields();
                for (Field destField : destFields) {
                    if (Modifier.isStatic(destField.getModifiers())) {
                        // 过滤掉static
                        continue;
                    }
                    destField.setAccessible(true);
                    if (srcMap.get(destField.getName()) == null) {
                        continue;
                    }
                    try {
                        destField.set(to, srcMap.get(destField.getName())); //给属性赋值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }


    }

}
