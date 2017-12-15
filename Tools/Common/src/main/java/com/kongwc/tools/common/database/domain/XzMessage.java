package com.kongwc.tools.common.database.domain;

import lombok.Data;

import java.util.Date;

/**
 * 广播信息
 */
@Data
public class XzMessage {

    private Integer id;
    private String messageId;
    private String messageTitle;
    private String message;
    private Date publishTime;
    private Integer status;
    private Integer fileExist;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
}
