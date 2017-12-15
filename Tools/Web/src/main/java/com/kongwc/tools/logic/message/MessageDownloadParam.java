package com.kongwc.tools.logic.message;

import com.kongwc.tools.common.data.ApiParameter;
import lombok.Data;

/**
 * 广播消息下载参数
 */
@Data
public class MessageDownloadParam extends ApiParameter {

    private String fileDir;
}
