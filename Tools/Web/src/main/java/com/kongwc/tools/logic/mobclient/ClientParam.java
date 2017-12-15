package com.kongwc.tools.logic.mobclient;

import com.kongwc.tools.common.data.ApiInquiryParameter;
import lombok.Data;

import java.sql.Blob;

/**
 * 手持终端参数
 */
@Data
public class ClientParam extends ApiInquiryParameter {

    private String clientId;
    private String imei;
    private String clientModel;
    private Integer status;
    private String lastOnlineTime;
    private String description;
    private Blob sdFile;
}
