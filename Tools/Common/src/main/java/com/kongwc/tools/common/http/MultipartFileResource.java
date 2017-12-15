package com.kongwc.tools.common.http;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * MultipartFile辅助类
 */
public class MultipartFileResource extends InputStreamResource {

    /**
     * 文件名
     */
    private String filename;

    /**
     * 构造函数
     * @param inputStream 输入流
     * @param filename 文件名
     */
    public MultipartFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    /**
     * 获取文件名
     * @return 文件名
     */
    @Override
    public String getFilename() {
        return this.filename;
    }

    /**
     * 获取内容长度
     * @return 长度
     * @throws IOException IO错误
     */
    @Override
    public long contentLength() throws IOException {
        return -1; // we do not want to generally read the whole stream into memory ...
    }
}
