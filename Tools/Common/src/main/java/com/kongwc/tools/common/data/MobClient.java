package com.kongwc.tools.common.data;

import lombok.Data;

import java.sql.Blob;

/**
 * 手持终端信息
 */
@Data
public class MobClient {

    private String clientId;
    private String imei;
    private String clientModel;
    private Integer status;
    private String lastOnlineTime;
    private String description;
    private Blob sdFile;
    private Integer batchAction;
}
