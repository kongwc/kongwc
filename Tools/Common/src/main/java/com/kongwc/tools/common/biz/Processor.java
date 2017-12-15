package com.kongwc.tools.common.biz;

import lombok.Data;

/**
 * 处理器父类
 */
@Data
public abstract class Processor<T, K> {

    /**
     * 心跳处理是否发生异常
     */
    private boolean isHeartbeatError;

    /**
     * 心跳处理异常信息
     */
    private String heartbeatErrorMessage;

    /**
     * 执行处理
     *
     * @param param 执行参数
     * @return 执行结果
     */
    public abstract K process(T param);

    /**
     * 心跳处理
     *
     * @return 心跳结果
     */
    public abstract boolean onHeartbeat();

}
