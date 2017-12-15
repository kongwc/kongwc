package com.kongwc.tools.common.data;

import lombok.Data;

/**
 * 广播信息
 */
@Data
public class Message {

    private String messageId;
    private String messageTitle;
    private String message;
    private String publishTime;
    private Integer status;
    private Integer attachment;
}
