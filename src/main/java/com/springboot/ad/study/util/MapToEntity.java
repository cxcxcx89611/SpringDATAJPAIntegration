package com.springboot.ad.study.util;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <h1>字段值到实体类的映射工具类</h1>
 * Created by Qinyi.
 */
public class MapToEntity {

    public static <T> T mapToEntity(Map<String, Object> map, Class<T> targetClass)
        throws IllegalAccessException, InstantiationException {

        // 存储父类
        Class superClass;
        // 存储父类中的属性
        Field[] fields;

        // new 一个实例对象
        T target = targetClass.newInstance();

        // 存储 targetClass 的所有 Field
        List<Field> targetFieldList = new LinkedList<>();

        // 先把目标类赋值给父类
        superClass = targetClass;

        while (superClass != null && superClass != Object.class) {

            fields = superClass.getDeclaredFields();
            // 存储当前类的所有属性字段
            targetFieldList.addAll(Arrays.asList(fields));
            // 获取父类
            superClass = superClass.getSuperclass();
        }

        for (Field targetField: targetFieldList) {

            for (Map.Entry<String, Object> mapEntry: map.entrySet()) {

                if (targetField.getName().equals(mapEntry.getKey())) {

                    // 暂时保存权限
                    boolean targetFlag = targetField.isAccessible();
                    // 赋予修改权限
                    targetField.setAccessible(true);
                    // 赋值
                    targetField.set(target, mapEntry.getValue() instanceof BigInteger ?
                            ((BigInteger) mapEntry.getValue()).longValue() : mapEntry.getValue());
                    // 恢复原权限
                    targetField.setAccessible(targetFlag);

                    break;
                }
            }
        }

        return target;
    }
}
