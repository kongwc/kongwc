package com.kongwc.tools.common.msg.converter;

import java.io.Serializable;
import java.util.List;

/**
 * MQ 消息转换器接口
 */
public interface MsgConverter {

    /**
     * Java对象转换为字符串
     *
     * @param obj Java对象
     * @param <T> 类型
     * @return 字符串
     */
    <T extends Object> String packObject(T obj);

    /**
     * 字符串转换为 Java对象
     *
     * @param objstr 字符串
     * @param clazz  对象Class
     * @param <T>    类型
     * @return Java对象
     */
    <T extends Object> T unpackObject(String objstr, Class<T> clazz);

    /**
     * Java对象List转换为字符串
     *
     * @param obj Java对象List
     * @param <T> 类型
     * @return 字符串
     */
    <T extends Object> String packList(List<T> obj);


    /**
     * 字符串转换为 Java对象List
     *
     * @param objstr 字符串
     * @param clazz  对象Class
     * @param <T>    类型
     * @return Java对象List
     */
    <T extends Object> List<T> unpackList(String objstr, Class<T> clazz);
}
