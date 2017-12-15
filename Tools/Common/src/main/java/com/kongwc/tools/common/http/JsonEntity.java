package com.kongwc.tools.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by luohuan on 2017/06/06.
 */
public class JsonEntity {

    public String toJson() {
        return JSON.toJSONString(this, SerializerFeature.DisableCheckSpecialChar);
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
