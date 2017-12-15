package com.kongwc.tools.common.msg.converter.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kongwc.tools.common.msg.converter.MsgConverter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MQ 消息转换器---Json实现
 */
@Component
@Primary
public class JsonConverter implements MsgConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Object> String packObject(T obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Object> T unpackObject(String objstr, Class<T> clazz) {
        return JSON.parseObject(objstr, clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Object> String packList(List<T> obj) {
        return JSONArray.toJSONString(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Object> List<T> unpackList(String objstr, Class<T> clazz) {
        return JSONArray.parseArray(objstr, clazz);
    }

}
