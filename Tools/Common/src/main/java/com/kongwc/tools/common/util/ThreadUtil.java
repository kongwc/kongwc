package com.kongwc.tools.common.util;

/**
 * 线程相关工具函数
 */
public abstract class ThreadUtil {

    /**
     * 获取所有StackTrace信息
     *
     * @return StackTrace信息
     */
    public static StackTraceElement[] getAllStackTraceElement() {

        return Thread.currentThread().getStackTrace();
    }

    /**
     * 获取当前StackTrace信息
     *
     * @return 当前StackTrace信息
     */
    public static StackTraceElement getCurrentStackTraceElement() {

        return Thread.currentThread().getStackTrace()[3];
    }

    /**
     * 获取调用源StackTrace信息
     *
     * @return 调用源StackTrace信息
     */
    public static StackTraceElement getCallerStackTraceElement() {

        return Thread.currentThread().getStackTrace()[4];
    }

    /**
     * 线程等待
     *
     * @param time 等待时间
     */
    public static void sleep(long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 线程等待
     *
     * @param time 等待时间
     */
    public static boolean sleepForInterrupt(long time) {

        try {
            Thread.sleep(time);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 线程等待
     *
     * @param time 等待时间
     */
    public static void executeOnDelay(long time, Runnable run) {

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (run != null) {
                            run.run();
                        }
                    }
                },
                time
        );
    }
}
