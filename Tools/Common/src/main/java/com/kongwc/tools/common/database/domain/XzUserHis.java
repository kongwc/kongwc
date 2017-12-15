package com.kongwc.tools.common.database.domain;

import lombok.Data;

import java.util.Date;

/**
 * 用户履历信息
 */
@Data
public class XzUserHis {

    private Integer id;
    private Integer hisId;
    private String userId;
    private String clientId;
    private Date loginTime;
    private Date logoutTime;
    private String ipAddr;
    private String description;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;


}
