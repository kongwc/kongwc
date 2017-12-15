package com.kongwc.tools.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 任务运行主机相关设定
 */
@Component
@PropertySource("classpath:host.properties")
@Data
@Configuration
public class ServerConfig {

    /**
     * 64位Dll设备连接主机ID地址
     */
    @Value("${host.dll.64bit.ip}")
    private String deviceHost64BitIpAddr;

    /**
     * 64位Dll设备连接主机端口
     */
    @Value("${host.dll.64bit.port}")
    private String deviceHost64BitPort;

    /**
     * 32位Dll设备连接主机ID地址
     */
    @Value("${host.dll.32bit.ip}")
    private String deviceHost32BitIpAddr;

    /**
     * 32位Dll设备连接主机端口
     */
    @Value("${host.dll.32bit.port}")
    private String deviceHost32BitPort;

    /**
     * API服务器主机ID地址
     */
    @Value("${host.api.ip}")
    private String apiHostIpAddr;

    /**
     * API服务器主机端口
     */
    @Value("${host.api.port}")
    private String apiHostPort;

//    /**
//     * 转码服务器主机ID地址
//     */
//    @Value("${host.trans.ip}")
//    private String transCodeSvrIpAddr;
//
//    /**
//     * 转码服务器主机端口
//     */
//    @Value("${host.trans.port}")
//    private String transCodeSvrPort;

//    /**
//     * 流媒体服务器主机ID地址
//     */
//    @Value("${host.media.ip}")
//    private String mediaSvrIpAddr;
//
//    /**
//     * 流媒体服务器主机端口
//     */
//    @Value("${host.media.port}")
//    private String mediaSvrPort;

//    /**
//     * 流媒体服务器服务节点ID
//     */
//    @Value("${host.media.service}")
//    private String mediaSvrService;

//    /**
//     * API服务器图片下载URL
//     */
//    @Value("${host.api.img}")
//    private String apiHostImg;

//    /**
//     * 推送服务器主机ID地址
//     */
//    @Value("${host.push.ip}")
//    private String pushHostIpAddr;
//
//    /**
//     * 推送服务器主机端口
//     */
//    @Value("${host.push.port}")
//    private String pushHostPort;

    /**
     * 运行任务的所有zookeeper主机的list
     */
    @Value("${host.job.ip}")
    private String jobServerList;

    /**
     * 任务的命名空间
     */
    @Value("${host.job.namespace}")
    private String jobNamespace;

}
