package com.kongwc.tools.logic.mobclient;

import com.kongwc.tools.common.data.ApiResult;
import com.kongwc.tools.common.data.MobClient;
import lombok.Data;

import java.util.List;

/**
 * 手持终端获取结果
 */
@Data
public class ClientGetResult extends ApiResult {

    private Long dataCount;
    private Long startIndex;
    private Long endIndex;
    private List<MobClient> mobClients;
}
