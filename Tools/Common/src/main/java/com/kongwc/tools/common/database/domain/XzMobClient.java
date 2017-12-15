package com.kongwc.tools.common.database.domain;

import lombok.Data;

import java.sql.Blob;
import java.util.Date;

/**
 * 手持终端信息
 */
@Data
public class XzMobClient {

    private Integer id;
    private String clientId;
    private String imei;
    private String clientModel;
    private Integer status;
    private Date lastOnlineTime;
    private Integer isDeleted;
    private String description;
    private Blob sdFile;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
}
