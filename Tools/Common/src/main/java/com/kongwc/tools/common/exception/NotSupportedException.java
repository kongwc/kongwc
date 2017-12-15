package com.kongwc.tools.common.exception;

/**
 * 未被实现异常
 */
public class NotSupportedException extends RuntimeException {

    /**
     * 构造函数
     *
     * @param message 消息
     */
    public NotSupportedException(String message) {
        super(message);
    }
}
