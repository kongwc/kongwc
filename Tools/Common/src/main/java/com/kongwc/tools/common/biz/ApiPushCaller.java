package com.kongwc.tools.common.biz;

import com.kongwc.tools.common.http.RestClient;
import com.kongwc.tools.common.config.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * API Service调用管理器
 */
@Component
@Slf4j
public class ApiPushCaller {

    /**
     * API 调用URL前缀
     */
    private static final String PREFIX_API = "/api";

    /**
     * 存储服务 调用URL前缀
     */
    private static final String PREFIX_STORAGE = "/storage";

    /**
     * HTTP 工具类
     */
    @Autowired
    private RestClient restClient;

    /**
     * 服务器主机相关配置
     */
    @Autowired
    private ServerConfig serverConfig;

    /**
     * API调用地址
     */
    private String apiUrl;

//    /**
//     * 存储服务调用地址
//     */
//    private String storageUrl;

//    /**
//     * PUSH调用地址
//     */
//    private String pushUrl;

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {

        // API服务器调用地址
        apiUrl = "http://".concat(serverConfig.getApiHostIpAddr())
                .concat(":").concat(serverConfig.getApiHostPort()).concat(PREFIX_API);

//        // 存储服务调用地址
//        storageUrl = "http://".concat(serverConfig.getApiHostIpAddr())
//                .concat(":").concat(serverConfig.getApiHostPort()).concat(PREFIX_STORAGE);

//        // 推送服务器调用地址
//        pushUrl = "http://".concat(serverConfig.getPushHostIpAddr())
//                .concat(":").concat(serverConfig.getPushHostPort());
    }


//    /**
//     * 数据推送
//     *
//     * @param data 数据
//     */
//    public void push(Object data, String pushPathType) {
//
//        // 发送推送请求
//        restClient.post(pushUrl, "/api/websocket/".concat(pushPathType).concat("/postjson"), JSON.toJSONString(data), ApiResult.class);
//    }
}
