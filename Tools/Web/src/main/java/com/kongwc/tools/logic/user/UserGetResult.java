package com.kongwc.tools.logic.user;

import com.kongwc.tools.common.data.ApiResult;
import com.kongwc.tools.common.data.User;
import lombok.Data;

import java.util.List;

/**
 * 用户获取结果
 */
@Data
public class UserGetResult extends ApiResult {

    private Long dataCount;
    private Long startIndex;
    private Long endIndex;
    private List<User> users;
}
