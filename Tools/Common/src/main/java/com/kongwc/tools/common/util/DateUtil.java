package com.kongwc.tools.common.util;

import com.kongwc.tools.common.exception.DevelopmentException;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期相关工具类
 */
public class DateUtil {

    /**
     * 默认日期格式
     */
    private static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 默认时间格式
     */
    private static final SimpleDateFormat defaultTimeFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 默认时间格式(精确到秒)
     */
    private static final SimpleDateFormat defaultTimeFormatBySecond = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 时间格式
     */
    private static final SimpleDateFormat defaultTimeOnlyFormat = new SimpleDateFormat("HHmmssSSS");

    /**
     * 获取当前系统时间
     *
     * @return 当前系统时间
     */
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取当前系统时间
     *
     * @return 当前系统时间
     */
    public static String getCurrentDateDefaultFormat() {
        Date systemDate = getCurrentDate();
        return defaultDateFormat.format(systemDate);
    }

    /**
     * 获取当前系统时间
     *
     * @return 时间
     */
    public static String getCurrentTimeDefaultFormat() {
        Date time = new Date();
        return defaultTimeFormat.format(time);
    }

    /**
     * 获取当前系统时间
     *
     * @return 时间
     */
    public static String getCurrentTimeOnlyDefaultFormat() {
        Date time = new Date();
        return defaultTimeOnlyFormat.format(time);
    }

    /**
     * 转换毫秒为时间
     *
     * @return 时间
     */
    public static String getTimeDefaultFormat(long ms) {
        Date time = new Date(ms);
        return defaultTimeFormat.format(time);
    }

    /**
     * 转换Date为时间
     *
     * @return 时间
     */
    public static String getTimeDefaultFormat(Date time) {
        if (time == null) {
            return null;
        }
        return defaultTimeFormat.format(time);
    }

    /**
     * 转换Date为时间(精确到秒)
     *
     * @return 时间
     */
    public static String getTimeDefaultFormatBySecond(Date time) {
        if (time == null) {
            return null;
        }
        return defaultTimeFormatBySecond.format(time);
    }

    /**
     * 转换Date为时间
     *
     * @return 时间
     */
    public static String getTimeOnlyDefaultFormat(Date time) {
        if (time == null) {
            return null;
        }
        return defaultTimeOnlyFormat.format(time);
    }

    /**
     * 将标准格式时间转化为日期
     *
     * @param str 标准格式时间
     * @return 日期
     */
    public static Date getDateFromDefaultFormat(String str) {
        try {
            return defaultTimeFormat.parse(str);
        } catch (ParseException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 将标准格式时间转化为日期(精确到秒)
     *
     * @param str 标准格式时间
     * @return 日期
     */
    public static Date getDateFromDefaultFormatBySecond(String str) {
        try {
            return defaultTimeFormatBySecond.parse(str);
        } catch (ParseException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 检查是否符合标准时间格式
     *
     * @param str 时间
     * @return 是否符合
     */
    public static boolean checkDefaultFormat(String str) {
        try {
            defaultTimeFormat.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
