package com.kongwc.tools.common.config;

import com.kongwc.tools.common.database.datasource.RDBDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

/**
 * RDBDatasource配置
 */
@Configuration
@PropertySource("classpath:database.properties")
public class RDBDataSourceConfig {

    /**
     * DB 连接
     */
    @Value("${spring.datasource.url}")
    private String dbUrl;

    /**
     * 用户名
     */
    @Value("${spring.datasource.username}")
    private String username;

    /**
     * 密码
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 驱动class名
     */
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * 创建RDBDataSource
     *
     * @return RDBDataSource
     */
    @Bean
    @Primary
    public RDBDataSource createRDBDataSource() {
        RDBDataSource datasource = new RDBDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        return datasource;
    }
}
