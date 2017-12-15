package com.kongwc.tools.common.exception;

import lombok.Getter;

/**
 * Web服务调用异常
 */
@Getter
public class ServiceCallException extends SystemException {

    /**
     * 错误代码
     */
    private String code;

    /**
     * 构造函数
     *
     * @param message 消息
     */
    public ServiceCallException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     *
     * @param message 消息
     */
    public ServiceCallException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
