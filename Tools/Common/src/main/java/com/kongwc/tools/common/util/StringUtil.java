package com.kongwc.tools.common.util;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * 字符串相关工具函数
 */
public class StringUtil {

    /**
     * 字符串连接
     *
     * @param c         输入
     * @param separator 分隔符
     * @return 结果
     */
    public static String join(List c, char separator) {
        if (c == null) {
            return null;
        }
        if (c.size() == 0) {
            return "";
        } else if (c.size() == 1) {
            return nvl(c.get(0));
        } else {
            return StringUtils.join(c, separator);
        }
    }

    /**
     * 字符串连接
     *
     * @param c         输入
     * @param separator 分隔符
     * @return 结果
     */
    public static String joinWithQuotation(List c, char separator) {
        if (c == null) {
            return null;
        }
        if (c.size() == 0) {
            return "";
        } else if (c.size() == 1) {
            return nvl(c.get(0));
        } else {

            StringBuilder sb = new StringBuilder();
            for (Object o : c) {
                sb.append('\'');
                sb.append(o);
                sb.append('\'');
                sb.append(separator);
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }

    /**
     * 字符串空转换
     *
     * @param input 输入字符串
     * @return 转换结果
     */
    public static String nvl(Object input) {
        return input == null ? "" : input.toString();
    }

    /**
     * 字符串空转换
     *
     * @param input        输入字符串
     * @param defaultValue 默认值
     * @return 转换结果
     */
    public static <T> T nvl(T input, T defaultValue) {
        return input == null ? defaultValue : input;
    }

    /**
     * 对小数数据的补位
     *
     * @param input   输入值
     * @param length  长度
     * @param padChar 补位符
     * @return 补位后的值
     */
    public static String padDecimalStr(String input, int length, char padChar) {
        if (input == null) {
            return null;
        }
        String result = input.contains(".") ? input : input.concat(".");
        result = StringUtils.rightPad(result, length, padChar);
        return StringUtils.left(result, length);
    }

    /**
     * 对小数数据的补位
     *
     * @param input         输入值
     * @param integerLength 整数部分长度
     * @param decimalLength 小数部分长度
     * @param padChar       补位符
     * @return 补位后的值
     */
    public static String padDecimalStr(String input, int integerLength, int decimalLength, char padChar) {
        if (input == null) {
            return null;
        }
        String intStr;
        String decimalStr;
        int index = input.indexOf(".");
        if (index < 0) {
            intStr = input;
            decimalStr = "";
        } else {
            intStr = input.substring(0, index);
            decimalStr = input.substring(index + 1);
        }
        return StringUtils.leftPad(intStr, integerLength, padChar).concat(".")
                .concat(StringUtils.rightPad(decimalStr, decimalLength, padChar));
    }

    /**
     * 获取随机数字
     *
     * @param count 数字位数
     * @return 随机数字
     */
    public static String getRandomNumber(int count) {
        Random random = new Random(System.currentTimeMillis());
        int number = random.nextInt((int) Math.pow(10, count));
        return StringUtils.leftPad(String.valueOf(number), count, '0');
    }

    /**
     * 对数字字符串进行检查
     *
     * @param str 字符串
     * @param min 最小值
     * @param max 最大值
     * @return 检查结果
     */
    public static boolean checkNumber(String str, String min, String max) {

        if (StringUtils.isNumeric(str)) {
            BigDecimal value = new BigDecimal(str);
            if (StringUtils.isNumeric(min)) {
                if (value.compareTo(new BigDecimal(min)) < 0) {
                    return false;
                }
            }
            if (StringUtils.isNumeric(max)) {
                if (value.compareTo(new BigDecimal(max)) > 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 对两个字符串进行比较
     *
     * @param src  源字符串
     * @param des  目标字符串
     * @param oper 比较操作符
     * @return 比较结果
     */
    public static boolean evaluate(String src, String des, String oper) {

        if (StringUtils.isEmpty(src) || StringUtils.isEmpty(des) || StringUtils.isEmpty(oper)) {
            return false;
        }
        if (StringUtil.isNumber(src) && StringUtil.isNumber(des)) {

            String intSrc = StringUtils.substringBefore(src, ".");
            String decimalSrc = StringUtils.substringAfter(src, ".");

            String intDes = StringUtils.substringBefore(des, ".");
            String decimalDes = StringUtils.substringAfter(des, ".");

            int maxIntLen = Math.max(intSrc.length(), intDes.length());
            int maxDecimalLen = Math.max(decimalSrc.length(), decimalDes.length());
            src = StringUtil.padDecimalStr(src, maxIntLen, maxDecimalLen, '0');
            des = StringUtil.padDecimalStr(des, maxIntLen, maxDecimalLen, '0');
        }

        if (">".equals(oper)) {
            return src.compareTo(des) > 0;
        } else if (">=".equals(oper)) {
            return src.compareTo(des) >= 0;
        } else if ("=".equals(oper)) {
            return src.compareTo(des) == 0;
        } else if ("<=".equals(oper)) {
            return src.compareTo(des) <= 0;
        } else if ("<".equals(oper)) {
            return src.compareTo(des) < 0;
        } else if ("LIKE".equalsIgnoreCase(oper)) {
            return src.contains(des);
        } else if ("IN".equalsIgnoreCase(oper)) {
            String[] arr = des.split(",");
            return ArrayUtils.contains(arr, src);
        }
        throw new IllegalArgumentException(String.format("Not supported operation:%s", oper));

    }

    /**
     * 判断是否是数字（包括小数点）
     *
     * @param number 判断对象
     * @return 是否是数字
     */
    public static boolean isNumber(String number) {
        int index = number.indexOf(".");
        if (index < 0) {
            return StringUtils.isNumeric(number);
        } else {
            String num1 = number.substring(0, index);
            String num2 = number.substring(index + 1);

            return StringUtils.isNumeric(num1) && StringUtils.isNumeric(num2);
        }
    }

}
