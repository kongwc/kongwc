package com.kongwc.tools.common.exception;

/**
 * HTTP访问异常
 */
public class HttpException extends SystemException {

    /**
     * 构造函数
     *
     * @param message 消息
     */
    public HttpException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 消息
     * @param cause 系统异常
     */
    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
