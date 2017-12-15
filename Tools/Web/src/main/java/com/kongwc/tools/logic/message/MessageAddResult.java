package com.kongwc.tools.logic.message;

import com.kongwc.tools.common.data.ApiResult;
import lombok.Data;

/**
 * 广播消息追加返回结果
 */
@Data
public class MessageAddResult extends ApiResult {

    private String messageId;
}
