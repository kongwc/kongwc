package com.kongwc.tools.common.database.accessor.impl;

import com.kongwc.tools.common.database.accessor.AbstractDaoSupport;
import com.kongwc.tools.common.database.datasource.CommonDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cassandra JDBC Template
 */
@Component
public class Cassandra extends AbstractDaoSupport {

    /**
     * 构造函数
     *
     * @param dataSource 数据源
     */
    public Cassandra(CommonDataSource dataSource) {
        super(dataSource);
    }

}
