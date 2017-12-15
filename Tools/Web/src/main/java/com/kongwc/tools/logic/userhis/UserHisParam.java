package com.kongwc.tools.logic.userhis;

import com.kongwc.tools.common.data.ApiParameter;
import lombok.Data;

/**
 * 用户登录履历参数
 */
@Data
public class UserHisParam extends ApiParameter {

    private Integer hisId;
    private String userId;
    private String clientId;
    private String loginTime;
    private String logoutTime;
    private String ipAddr;
    private String description;
}
