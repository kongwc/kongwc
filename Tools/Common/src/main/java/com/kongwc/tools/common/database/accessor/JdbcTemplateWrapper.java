package com.kongwc.tools.common.database.accessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.*;

/**
 * JdbcTemplate包装类
 */
@Slf4j
public class JdbcTemplateWrapper extends JdbcTemplate {

    /**
     * 构造函数
     *
     * @param dataSource 数据源
     */
    public JdbcTemplateWrapper(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {

        long startTime = System.currentTimeMillis();
        List<T> result = null;
        try {
            result = super.query(sql, rowMapper, args);
        } finally {
            logSqlExecuted(sql, args, startTime, result != null ? String.valueOf(result.size()) : "0");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> queryForList(String sql, Class<T> clazz, Object... args) {

        long startTime = System.currentTimeMillis();
        List<T> result = null;
        try {
            result = super.queryForList(sql, clazz, args);
        } finally {
            logSqlExecuted(sql, args, startTime, result != null ? String.valueOf(result.size()) : "0");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {

        long startTime = System.currentTimeMillis();
        T result = null;
        try {
            result = super.queryForObject(sql, args, rowMapper);
        } finally {
            logSqlExecuted(sql, args, startTime, result == null ? "null" : "1");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> queryForMap(String sql, Object... args) {

        long startTime = System.currentTimeMillis();
        Map<String, Object> result= null;
        try {
            result = super.queryForMap(sql, args);
        } finally {
            logSqlExecuted(sql, args, startTime, result == null ? "null" : "1");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(String sql, Object... args) {
        long startTime = System.currentTimeMillis();
        int count = 0;
        try {
            count = super.update(sql, args);
        } finally {
            logSqlExecuted(sql, args, startTime, String.valueOf(count));
        }
        return count;
    }

    /**
     * SQL 结束log输出
     *
     * @param sql         SQL文
     * @param args        参数
     * @param startTime   开始时间
     * @param resultCount 结果件数
     */
    private void logSqlExecuted(String sql, Object[] args, long startTime, String resultCount) {
        if (log.isInfoEnabled()) {
            log.info("Executed sql [{}] with {} in {}ms, result count is {}", sql, Arrays.toString(args), System.currentTimeMillis() - startTime, resultCount);
        }
    }
}
