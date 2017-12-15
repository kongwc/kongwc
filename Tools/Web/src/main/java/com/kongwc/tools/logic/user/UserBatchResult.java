package com.kongwc.tools.logic.user;

import com.kongwc.tools.common.data.ApiResult;
import lombok.Data;

import java.util.List;

/**
 * 用户信息结果
 */
@Data
public class UserBatchResult extends ApiResult {

    private List<String> userIds;
}
