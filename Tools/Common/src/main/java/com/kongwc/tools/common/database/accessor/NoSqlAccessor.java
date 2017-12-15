package com.kongwc.tools.common.database.accessor;

import com.kongwc.tools.common.database.datasource.NoSqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Nosql数据库访问器
 */
@Component
public class NoSqlAccessor extends AbstractAccessor {

    /**
     * 构造函数
     *
     * @param dataSource 数据源
     */
    @Autowired
    public NoSqlAccessor(NoSqlDataSource dataSource) {
        super(dataSource);
    }

}
