package com.kongwc.tools.common.data;

import lombok.Data;

/**
 * 用户登陆履历信息
 */
@Data
public class UserHis {

    private Integer hisId;
    private String userId;
    private String clientId;
    private String loginTime;
    private String logoutTime;
    private String ipAddr;
    private String description;
}
