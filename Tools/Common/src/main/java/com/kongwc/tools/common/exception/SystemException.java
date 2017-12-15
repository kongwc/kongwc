package com.kongwc.tools.common.exception;

/**
 * 系统异常
 */
public class SystemException extends RuntimeException {

    /**
     * 构造函数
     *
     * @param message 消息
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param cause 系统异常
     */
    public SystemException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param message 消息
     * @param cause 系统异常
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

}
