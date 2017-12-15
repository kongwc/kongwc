package com.kongwc.tools.common.config;

import com.kongwc.tools.common.database.datasource.NoSqlDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotationMetadata;

/**
 * NosqlDatasource配置
 */
@Configuration
@PropertySource("classpath:database.properties")
public class NoSqlDataSourceConfig {

    /**
     * DB 连接
     */
    @Value("${spring.datasource.nosql.url}")
    private String dbUrl;

    /**
     * 用户名
     */
    @Value("${spring.datasource.nosql.username}")
    private String username;

    /**
     * 密码
     */
    @Value("${spring.datasource.nosql.password}")
    private String password;

    /**
     * 驱动class名
     */
    @Value("${spring.datasource.nosql.driver-class-name}")
    private String driverClassName;

    /**
     * 创建NosqlDataSource
     *
     * @return NosqlDataSource
     */
    @Bean
    public NoSqlDataSource createNoSqlDataSource() {
        NoSqlDataSource datasource = new NoSqlDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        return datasource;
    }

}
