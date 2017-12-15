package com.kongwc.tools.logic.mobclient;

import com.kongwc.tools.common.data.ApiParameter;
import com.kongwc.tools.common.data.MobClient;
import lombok.Data;

import java.util.List;

/**
 * 手持终端信息参数
 */
@Data
public class MobClientBatchParam extends ApiParameter {

    private List<MobClient> mobClients;
}
