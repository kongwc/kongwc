package com.kongwc.tools.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 文件存储相关配置
 */
@Data
@Configuration
@PropertySource("classpath:file.properties")
public class FileConfig {

    /**
     * 视频分类
     */
    @Value("${file.type.video}")
    private String fileTypeVideo;

    /**
     * 图片分类
     */
    @Value("${file.type.image}")
    private String fileTypeImage;

    /**
     * 文本分类
     */
    @Value("${file.type.text}")
    private String fileTypeText;

    /**
     * Word分类
     */
    @Value("${file.type.word}")
    private String fileTypeWord;

    /**
     * Excel分类
     */
    @Value("${file.type.excel}")
    private String fileTypeExcel;
}
