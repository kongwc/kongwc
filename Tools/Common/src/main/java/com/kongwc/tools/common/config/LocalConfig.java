package com.kongwc.tools.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 本机相关配置
 */
@Data
@Configuration
public class LocalConfig {

    /**
     * 本机端口
     */
    @Value("${server.ip:none}")
    private String ip;

    /**
     * 本机端口
     */
    @Value("${server.port}")
    private String port;

    /**
     * 文件存储目录
     */
    @Value("${file.storage.dir}")
    private String fileStorageDir;

    /**
     * 网速测试用文件夹
     */
    @Value("${file.storage.netSpeedTest.folder}")
    private String fileNetSpeedTestFolder;
}
