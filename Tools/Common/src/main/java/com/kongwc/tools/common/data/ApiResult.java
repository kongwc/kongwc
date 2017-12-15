package com.kongwc.tools.common.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 返回结果父类
 */
@Data
@NoArgsConstructor
public class ApiResult extends WebServiceResult {

    public ApiResult(int returnCode, String errorMessage) {
        this.returnCode = returnCode;
        this.errorMessage = errorMessage;
    }
    private int returnCode;
    private String errorMessage;

    private Boolean isFake;
    private String fakeData;
}
