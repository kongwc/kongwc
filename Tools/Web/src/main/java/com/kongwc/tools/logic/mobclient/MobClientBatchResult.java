package com.kongwc.tools.logic.mobclient;

import com.kongwc.tools.common.data.ApiResult;
import lombok.Data;

import java.util.List;

/**
 * 手持终端信息结果
 */
@Data
public class MobClientBatchResult extends ApiResult {

    private List<String> clientIds;
}
