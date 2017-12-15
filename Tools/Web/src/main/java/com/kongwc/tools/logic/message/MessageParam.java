package com.kongwc.tools.logic.message;

import com.kongwc.tools.common.data.ApiInquiryParameter;
import lombok.Data;

/**
 * 广播信息参数
 */
@Data
public class MessageParam extends ApiInquiryParameter {

    private String messageId;
    private String messageTitle;
    private String message;
    private String publishTime;
    private Integer status;
}
