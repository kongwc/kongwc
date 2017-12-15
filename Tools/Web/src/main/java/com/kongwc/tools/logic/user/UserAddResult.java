package com.kongwc.tools.logic.user;

import com.kongwc.tools.common.data.ApiResult;
import lombok.Data;

/**
 * 用户追加返回结果
 */
@Data
public class UserAddResult extends ApiResult {

    private String userId;
}
