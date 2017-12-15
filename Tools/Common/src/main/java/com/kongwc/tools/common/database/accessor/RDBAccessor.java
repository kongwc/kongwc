package com.kongwc.tools.common.database.accessor;

import com.kongwc.tools.common.database.datasource.RDBDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 关系型数据库访问器
 */
@Component
public class RDBAccessor extends AbstractAccessor {

    /**
     * 构造函数
     *
     * @param dataSource 数据源
     */
    @Autowired
    public RDBAccessor(RDBDataSource dataSource) {
        super(dataSource);
    }
}
