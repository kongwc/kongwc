package com.kongwc.tools.common.database.domain;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 */
@Data
public class XzUser {

    private Integer id;
    private String userId;
    private String userName;
    private String password;
    private String clientId;
    private Integer status;
    private Date lastLoginTime;
    private String description;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;


}
