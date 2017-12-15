package com.kongwc.tools.common.exception;

/**
 * 开发异常
 */
public class DevelopmentException extends SystemException {

    /**
     * 构造函数
     *
     * @param message 消息
     */
    public DevelopmentException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 消息
     * @param cause 系统异常
     */
    public DevelopmentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param cause 系统异常
     */
    public DevelopmentException(Throwable cause) {
        super(cause);
    }
}
