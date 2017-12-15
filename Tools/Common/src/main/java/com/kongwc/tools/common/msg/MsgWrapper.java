package com.kongwc.tools.common.msg;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息数据包装类
 */
@Data
@NoArgsConstructor
public class MsgWrapper {

    public static final MsgWrapper NONE = new MsgWrapper(-1, null, null);

    private String time;
    private int dataType;
    private Object message;
    private String imageBase64;
    private String dataClass;
    private String msgSetKey;
//    private String originImageUrl;
    private String recogPlanId;
    private String compPlanId;

    public MsgWrapper(int dataType, Object message, String imageBase64, String msgSetKey) {
        this.dataType = dataType;
        this.message = message;
        if (message != null){
            this.dataClass = message.getClass().getCanonicalName();
        }
        this.imageBase64 = imageBase64;
        this.msgSetKey = msgSetKey;
    }

    public MsgWrapper(int dataType, Object message, String imageBase64) {
        this(dataType, message, imageBase64, null);
    }

//    public String toJson() {
//        JSONObject jsonObject = (JSONObject)JSON.toJSON(message);
//        jsonObject.put("dataType", dataType);
//        jsonObject.put("imageBase64", imageBase64);
//        return JSON.toJSONString(jsonObject);
//    }

}
