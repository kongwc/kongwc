package com.kongwc.tools.logic.mobclient;

import com.kongwc.tools.common.data.ApiResult;
import lombok.Data;

/**
 * 手持终端追加返回结果
 */
@Data
public class MobClientAddResult extends ApiResult {

    private String clientId;
}
