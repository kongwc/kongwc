package com.kongwc.tools.common.exception;

/**
 * API未找到错误异常
 */
public class ApiNotFoundException extends SystemException {

    /**
     * 构造函数
     *
     * @param message 消息
     */
    public ApiNotFoundException(String message) {
        super(message);
    }

}
