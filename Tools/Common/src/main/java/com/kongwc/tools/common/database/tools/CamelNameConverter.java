package com.kongwc.tools.common.database.tools;

import org.springframework.stereotype.Component;

/**
 * DB字段名和Java名的相互转换（下划线命名和驼峰命名转换）
 */
@Component
public class CamelNameConverter implements FieldNameConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public String toDbName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }

        return result.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toJavaName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";

            // 不含下划线
        } else if (!name.contains("_")) {

            // 若全大写
            if (name.equals(name.toUpperCase())) {
                // 则转成全小写
                return name.toLowerCase();

                // 否则不用转换
            } else {
                return name;
            }
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
}
