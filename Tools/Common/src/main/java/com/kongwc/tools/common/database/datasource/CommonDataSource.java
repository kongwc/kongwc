package com.kongwc.tools.common.database.datasource;

import com.alibaba.druid.pool.DruidDataSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据源
 */
public class CommonDataSource extends DruidDataSource {

    /**
     * DB类型判断用正则表达式
     */
    private static Pattern JDBC_TYPE_PATTERN = Pattern.compile("jdbc:\\s*([\\w]+)\\s*:");

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDbType() {

        Matcher matcher = JDBC_TYPE_PATTERN.matcher(getUrl());
        if (matcher.find()) return matcher.group(1);
        return super.getDbType();
    }

}
