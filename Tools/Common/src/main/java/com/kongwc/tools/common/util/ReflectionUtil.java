package com.kongwc.tools.common.util;

import com.alibaba.fastjson.JSON;
import com.kongwc.tools.common.database.tools.FieldNameConverter;
import com.kongwc.tools.common.exception.DevelopmentException;
import com.kongwc.tools.common.exception.NotSupportedException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射相关处理函数
 */
public class ReflectionUtil {

    /**
     * Class类型的缓存
     */
    private static Map<String, Class<?>> clazzCache = new HashMap<>();

    /**
     * 根据类名加载类
     *
     * @param className 类名
     * @return 类
     */
    public static Class<?> forName(String className) {

        Class<?> clazz = clazzCache.get(className);
        if (clazz == null) {
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new DevelopmentException(e);
            }
            clazzCache.put(className, clazz);
        }
        return clazz;
    }

    /**
     * 根据属性转换规则在两个Object之间拷贝值
     *
     * @param src         拷贝源Object
     * @param dest        拷贝目标Object
     * @param mappingRule 转换规则
     */
    public static void mappingAttr(Map<String, Object> src, Object dest, Map<String, String> mappingRule, FieldNameConverter fieldNameConverter) {

        for (Map.Entry<String, Object> entry : src.entrySet()) {

            String srcFieldName = entry.getKey();
            if (mappingRule.containsKey(srcFieldName)) {
                Object srcValue = entry.getValue();
                String destFieldName = fieldNameConverter.toJavaName(mappingRule.get(srcFieldName));
                ReflectionUtil.setField(dest, destFieldName, srcValue);
            }
        }
    }

    /**
     * 根据属性转换规则在两个Object之间拷贝值
     *
     * @param src         拷贝源Object
     * @param dest        拷贝目标Object
     * @param mappingRule 转换规则
     */
    public static void mappingAttr(Object src, Object dest, Map<String, String> mappingRule, FieldNameConverter fieldNameConverter) {

        ReflectionUtils.doWithFields(src.getClass(), new ReflectionUtils.FieldCallback() {

            @Override
            public void doWith(Field srcField) throws IllegalArgumentException, IllegalAccessException {

                String srcFieldName = srcField.getName();
                if (mappingRule.containsKey(srcFieldName)) {
                    ReflectionUtils.makeAccessible(srcField);
                    Object srcValue = srcField.get(src);

                    String destFieldName = fieldNameConverter.toJavaName(mappingRule.get(srcFieldName));
                    Field destField = ReflectionUtils.findField(dest.getClass(), destFieldName);
                    ReflectionUtils.makeAccessible(destField);
                    destField.set(dest, srcValue);
                }
            }
        });
    }

    /**
     * 根据属性转换规则在获取Object的属性
     *
     * @param src         拷贝源Object
     * @param mappingRule 转换规则
     */
    public static Map<String, Object> mappingAttr(Map<String, Object> src, Map<String, String> mappingRule, FieldNameConverter fieldNameConverter) {

        Map<String, Object> attrs = new HashMap<>();
        for (Map.Entry<String, Object> entry : src.entrySet()) {

            String srcFieldName = entry.getKey();
            if (mappingRule.containsKey(srcFieldName)) {
                Object srcValue = entry.getValue();
                String destFieldName = fieldNameConverter.toJavaName(mappingRule.get(srcFieldName));
                attrs.put(destFieldName, srcValue);
            }
        }
        return attrs;
    }

    /**
     * 根据属性转换规则在获取Object的属性
     *
     * @param src         拷贝源Object
     * @param mappingRule 转换规则
     */
    public static Map<String, Object> mappingAttr(Object src, Map<String, String> mappingRule, FieldNameConverter fieldNameConverter) {

        Map<String, Object> attrs = new HashMap<>();
        ReflectionUtils.doWithFields(src.getClass(), new ReflectionUtils.FieldCallback() {

            @Override
            public void doWith(Field srcField) throws IllegalArgumentException, IllegalAccessException {

                String srcFieldName = srcField.getName();
                if (mappingRule.containsKey(srcFieldName)) {
                    ReflectionUtils.makeAccessible(srcField);
                    Object srcValue = srcField.get(src);

                    String destFieldName = fieldNameConverter.toJavaName(mappingRule.get(srcFieldName));
                    attrs.put(destFieldName, srcValue);
                }
            }
        });
        return attrs;
    }

    /**
     * 创建Class实例
     *
     * @param clazz          类
     * @param parameterTypes 构造函数类型
     * @param args           构造函数参数
     * @param <T>            类型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz, Class<?>[] parameterTypes,
                                    Object[] args) {
        try {
            return clazz.getConstructor(parameterTypes).newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 将Map映射到Bean
     *
     * @param bean       bean实例
     * @param properties 属性map
     */
    public static void populateBean(Object bean, Map<String, Object> properties, FieldNameConverter converter) {

        if (properties == null) {
            return;
        }

        String currentName = null;
        Object currentValue = null;
        Field currentField = null;
        try {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                currentName = entry.getKey();
                currentValue = entry.getValue();
                if (currentName == null) {
                    continue;
                }
                currentField = ReflectionUtils.findField(
                        bean.getClass(), converter == null ? currentName : converter.toJavaName(currentName));
                if (currentField == null) {
                    continue;
                }
                ReflectionUtils.makeAccessible(currentField);
                Class type = currentField.getType();
                Object newValue = null;
                if (type == int.class || type == Integer.class) {
                    newValue = Integer.parseInt(String.valueOf(currentValue));
                } else if (type == long.class || type == Long.class) {
                    newValue = Long.parseLong(String.valueOf(currentValue));
                } else if (type == float.class || type == Float.class) {
                    newValue = Float.parseFloat(String.valueOf(currentValue));
                } else if (type == double.class || type == Double.class) {
                    newValue = Double.parseDouble(String.valueOf(currentValue));
                } else if (type == String.class) {
                    newValue = String.valueOf(currentValue);
                } else if (!type.isPrimitive()) {
                    if (type != String.class && currentValue instanceof String) {
                        newValue = JSON.parseObject((String)currentValue, type);
                    } else {
                        newValue = currentValue;
                    }
                }
                ReflectionUtils.setField(currentField, bean, newValue);
            }

        } catch (IllegalArgumentException e) {
            throw new DevelopmentException(String.format("[%s] has wrong value. needs:%s found:%s",
                    currentName, currentField.getType().getSimpleName(), currentValue), e);
        }
    }

    /**
     * 将Bean映射到Map
     *
     * @param bean      bean实例
     * @param converter 转换器
     */
    public static Map<String, Object> describeBean(Object bean, FieldNameConverter converter, String... fieldNameStartsWith) {

        Map<String, Object> map = new HashMap<>();
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {

            @Override
            public void doWith(Field srcField) throws IllegalArgumentException, IllegalAccessException {

                ReflectionUtils.makeAccessible(srcField);
                String srcFieldName = srcField.getName();
                Object value = ReflectionUtils.getField(srcField, bean);
                if (value != null) {
                    map.put(converter == null ? srcFieldName : converter.toDbName(srcFieldName), value);
                }
            }
        }, fieldNameStartsWith == null || fieldNameStartsWith.length == 0 ? null : new ReflectionUtils.FieldFilter() {
            @Override
            public boolean matches(Field field) {

                for (String fieldNameStart : fieldNameStartsWith) {
                    if (field.getName().startsWith(fieldNameStart)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return map;

    }

    /**
     * 复制属性
     *
     * @param src 复制源
     * @param des 复制目标
     */
    public static void copyFields(Object src, Object des) {
        Map<String, Object> values = describeBean(src, null);
        populateBean(des, values, null);
    }

    /**
     * 将Bean映射到MultiValueMap
     *
     * @param bean      bean实例
     * @param converter 转换器
     */
    public static MultiValueMap<String, Object> describeBeanToFormMap(Object bean, FieldNameConverter converter, String... fieldNameStartsWith) {

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.setAll(describeBean(bean, converter, fieldNameStartsWith));
        return map;

    }

    /**
     * 对象设定属性
     *
     * @param bean      对象Object
     * @param fieldName 属性名
     * @param value     属性值
     */
    public static void setField(Object bean, String fieldName, Object value) {
        Field currentField = ReflectionUtils.findField(bean.getClass(), fieldName);
        if (currentField == null) {
            return;
        }
        ReflectionUtils.makeAccessible(currentField);
        ReflectionUtils.setField(currentField, bean, value);
    }

    /**
     * 对象设定属性
     *
     * @param bean      对象Object
     * @param fieldName 属性名
     * @param value     属性值
     * @param converter 名称转换
     */
    public static void setField(Object bean, String fieldName, Object value, FieldNameConverter converter) {
        setField(bean, converter.toJavaName(fieldName), value);
    }

    /**
     * 获取对象属性值
     *
     * @param bean      对象Object
     * @param fieldName 属性名
     */
    public static <T> T getField(Object bean, String fieldName) {
        Field currentField = ReflectionUtils.findField(bean.getClass(), fieldName);
        if (currentField == null) {
            return null;
        }
        ReflectionUtils.makeAccessible(currentField);
        return (T) ReflectionUtils.getField(currentField, bean);
    }

    /**
     * 判断是否基础类型
     *
     * @param type 类型
     * @return 是否基础类型
     */
    public static boolean isPrimitive(Class<?> type) {
        if (type.isPrimitive()) {
            return true;
        }
        if (Long.class.equals(type) || Integer.class.equals(type) || Double.class.equals(type) || Float.class.equals(type)) {
            return true;
        }
        return false;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    @SuppressWarnings("unchecked")
    public static Class<?> getTClass(Class<?> clazz) {

        Type t = clazz.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) t).getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    @SuppressWarnings("unchecked")
    public static Class<?> getKClass(Class<?> clazz) {

        Type t = clazz.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) t).getActualTypeArguments()[1];
        }
        return null;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    @SuppressWarnings("unchecked")
    public static Class<?> getInterfaceTClass(Class<?> clazz, Class<?> interfaceClass) {

        Type[] types = clazz.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (parameterizedType.getRawType().getTypeName().equals(interfaceClass.getTypeName())) {
                    return (Class<?>) (parameterizedType.getActualTypeArguments()[0]);
                }
            }
        }
        return null;
    }

    /**
     * 根据方法名执行方法
     *
     * @param methodName 方法名
     * @param obj        对象
     * @param params     执行参数
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(String methodName, Object obj, Object... params) {

        Method classMethod = ReflectionUtils.findMethod(obj.getClass(), methodName, (Class<?>[]) null);
        if (classMethod == null) {
            throw new NotSupportedException(String.format("Method not found.%s:", methodName));
        } else {
            return (T) ReflectionUtils.invokeMethod(classMethod, obj, params);
        }
    }

    /**
     * 查找方法
     *
     * @param clazz      对象类型
     * @param methodName 方法名
     * @return 方法
     */
    public static Method findMethod(Class<?> clazz, String methodName) {

        Method[] classMethods = clazz.getMethods();
        for (Method method : classMethods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
