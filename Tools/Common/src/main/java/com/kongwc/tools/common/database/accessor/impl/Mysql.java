package com.kongwc.tools.common.database.accessor.impl;

import com.kongwc.tools.common.database.accessor.AbstractDaoSupport;
import com.kongwc.tools.common.database.datasource.CommonDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Mysql JDBC Template
 */
@Component
public class Mysql extends AbstractDaoSupport {

    /**
     * 构造函数
     *
     * @param dataSource 数据源
     */
    public Mysql(CommonDataSource dataSource) {
        super(dataSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String makeSql4Insert(String table, Map<String, Object> columnValues) {

        String sql = super.makeSql4Insert(table, columnValues);
        return sql.replaceFirst("INSERT", "REPLACE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String makeSql4Execute(String sql) {
        String newSql = super.makeSql4Execute(sql);
        return newSql.replaceFirst("INSERT", "REPLACE");
    }

}
