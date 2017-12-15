package com.kongwc.tools.logic.user;

import com.kongwc.tools.common.data.ApiInquiryParameter;
import lombok.Data;

/**
 * 用户参数
 */
@Data
public class UserParam extends ApiInquiryParameter {

    private String userId;
    private String userName;
    private String pass;
    private String clientId;
    private Integer status;
    private String lastLoginTime;
    private String description;
}
