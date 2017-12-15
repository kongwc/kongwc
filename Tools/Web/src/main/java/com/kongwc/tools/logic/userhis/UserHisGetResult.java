package com.kongwc.tools.logic.userhis;

import com.kongwc.tools.common.data.ApiResult;
import com.kongwc.tools.common.data.UserHis;
import lombok.Data;

import java.util.List;

/**
 * 用户登陆履历获取结果
 */
@Data
public class UserHisGetResult extends ApiResult {

    private List<UserHis> userHises;
}
