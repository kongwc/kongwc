package com.kongwc.tools.common.data;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 */
@Data
public class User {

    private String userId;
    private String userName;
    private String pass;
    private String confirmPassword;
    private String clientId;
    private Integer status;
    private String lastLoginTime;
    private String description;
    private Integer batchAction;
}
