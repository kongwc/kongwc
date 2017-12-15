package com.kongwc.tools.logic.message;

import com.kongwc.tools.common.data.ApiResult;
import com.kongwc.tools.common.data.Message;
import lombok.Data;

import java.util.List;

/**
 * 广播信息获取结果
 */
@Data
public class MessageGetResult extends ApiResult {

    private Long dataCount;
    private Long startIndex;
    private Long endIndex;
    private List<Message> messages;
}
