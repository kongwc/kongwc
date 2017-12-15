//package com.liandisys.viewdepot.common.config;
//
//import com.liandisys.viewdepot.common.database.datasource.JobDataSource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//
///**
// * JobDatasource配置
// */
//@Configuration
//@PropertySource("classpath:database.properties")
//public class JobDataSourceConfig {
//
//    /**
//     * DB 连接
//     */
//    @Value("${spring.datasource.job.url}")
//    private String dbUrl;
//
//    /**
//     * 用户名
//     */
//    @Value("${spring.datasource.job.username}")
//    private String username;
//
//    /**
//     * 密码
//     */
//    @Value("${spring.datasource.job.password}")
//    private String password;
//
//    /**
//     * 驱动class名
//     */
//    @Value("${spring.datasource.job.driver-class-name}")
//    private String driverClassName;
//
//    /**
//     * 创建JobDataSource
//     *
//     * @return JobDataSource
//     */
//    @Bean
//    public JobDataSource createJobDataSource() {
//        JobDataSource datasource = new JobDataSource();
//        datasource.setUrl(this.dbUrl);
//        datasource.setUsername(username);
//        datasource.setPassword(password);
//        datasource.setDriverClassName(driverClassName);
//
//        return datasource;
//    }
//
//}
