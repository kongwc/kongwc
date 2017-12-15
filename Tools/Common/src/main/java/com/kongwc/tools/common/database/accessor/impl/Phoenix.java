package com.kongwc.tools.common.database.accessor.impl;

import com.kongwc.tools.common.database.accessor.AbstractDaoSupport;
import com.kongwc.tools.common.database.datasource.CommonDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Phoenix JDBC Template
 */
@Component
public class Phoenix extends AbstractDaoSupport {

    /**
     * 构造函数
     *
     * @param dataSource 数据源
     */
    public Phoenix(CommonDataSource dataSource) {
        super(dataSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(String table, Map<String, Object> columnValues, String where, Object... args) {

        int cnt = 0;

        // 获取主键名称
        String[] pks = getPrimaryKeys(table);

        // 生成查询更新对象记录所对应的主键值的SQL文
        String selectPk = makeSql4Select(table, pks, where);

        // 查询更新对象记录所对应的主键值
        SqlRowSet pkRowSet = this.getJdbcTemplate().queryForRowSet(selectPk, args);

        // 根据每组主键更新记录
        while (pkRowSet.next()) {
            for (String pk : pks) {
                columnValues.put(pk, pkRowSet.getObject(pk));
            }
            cnt = cnt + insert(table, columnValues);
        }
        return cnt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String makeSql4Insert(String table, Map<String, Object> columnValues) {

        String sql = super.makeSql4Insert(table, columnValues);
        return sql.replaceFirst("INSERT", "UPSERT");
    }

    /**
     * 获取主键名称
     *
     * @param table 表
     * @return 主键名称
     */
    private String[] getPrimaryKeys(String table) {

        Connection conn = null;
        try {
            conn = getConnection();
            DatabaseMetaData dbMeta = conn.getMetaData();
            ResultSet rs = dbMeta.getPrimaryKeys(conn.getCatalog(), null, table.toUpperCase());
            List<String> pkNames = new ArrayList<String>();
            while (rs.next()) {
                pkNames.add(rs.getString("COLUMN_NAME"));
            }
            return pkNames.toArray(new String[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            releaseConnection(conn);
        }
    }
}
