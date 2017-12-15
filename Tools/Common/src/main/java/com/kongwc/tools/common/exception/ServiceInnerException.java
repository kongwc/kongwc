package com.kongwc.tools.common.exception;

import lombok.Getter;

/**
 * Web服务内部异常
 */
@Getter
public class ServiceInnerException extends SystemException {

    /**
     * 错误代码
     */
    private String code;

    /**
     * 构造函数
     *
     * @param message 消息
     */
    public ServiceInnerException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     *
     * @param message 消息
     */
    public ServiceInnerException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
