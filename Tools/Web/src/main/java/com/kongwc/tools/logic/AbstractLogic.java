package com.kongwc.tools.logic;

import com.kongwc.tools.common.config.LocalConfig;
import com.kongwc.tools.common.database.tools.FieldNameConverter;
import com.kongwc.tools.common.exception.DevelopmentException;
import com.kongwc.tools.common.util.DateUtil;
import com.kongwc.tools.common.util.ReflectionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Logic 父类
 */
@Service
@Slf4j
public abstract class AbstractLogic {

    /**
     * 调用者信息
     */
    @Getter
    @Setter
    private String caller;

    /**
     * 本主机配置文件
     */
    @Autowired
    private LocalConfig localConfig;


    /**
     * 字段名转换器
     */
    @Autowired
    private FieldNameConverter nameConverter;

    /**
     * 设定更新者更新日等字段
     */
    protected void setUserTimeForCreate(Object bean) {
        ReflectionUtil.setField(bean, "CREATE_USER", caller, nameConverter);
        ReflectionUtil.setField(bean, "CREATE_TIME", DateUtil.getCurrentDate(), nameConverter);
        ReflectionUtil.setField(bean, "UPDATE_USER", caller, nameConverter);
        ReflectionUtil.setField(bean, "UPDATE_TIME", DateUtil.getCurrentDate(), nameConverter);
    }

    /**
     * 设定更新者更新日等字段
     */
    protected void setUserTimeForUpdate(Object bean) {
        ReflectionUtil.setField(bean, "UPDATE_USER", caller, nameConverter);
        ReflectionUtil.setField(bean, "UPDATE_TIME", DateUtil.getCurrentDate(), nameConverter);
    }

    /**
     * 上传文件
     *
     * @param file        文件
     * @param packageName 文件夹包名
     * @param isOnly      文件夹中文件是否唯一
     */
    public int uploadFile(MultipartFile file, String packageName, boolean isOnly) {

        if (file == null) {
            return -1;
        }
        File uploadFile = new File(
                localConfig.getFileStorageDir() + packageName, getOriginalFileName(file.getOriginalFilename()));
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        } else {
            if (isOnly) {
                File[] files = uploadFile.getParentFile().listFiles();
                if (files != null) {
                    for (File childFile : files) {
                        childFile.delete();
                    }
                }
            }
        }

        //创建输出流
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(uploadFile);
            //写入数据
            outStream.write(file.getBytes());
            //关闭输出流
            outStream.close();
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    /**
     * 文件下载
     *
     * @param fileDir  文件路径
     * @param fileName 文件名
     * @param response response
     */
    public int downloadFile(String fileDir, String fileName, HttpServletResponse response) {

        String childPath = fileDir;
        if (StringUtils.isNotEmpty(fileName)) {
            childPath = localConfig.getFileNetSpeedTestFolder();
        } else {
            File baseDir = new File(localConfig.getFileStorageDir() + childPath);
            File[] listFiles = baseDir.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                return -1;
            }
            fileName = listFiles[0].getName();
        }
        File file = new File(localConfig.getFileStorageDir() + childPath, fileName);
        if (!file.exists()) {
            return -1;
        }
        try {
            response.addHeader(
                    "Content-disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new DevelopmentException(e);
        }
        response.setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());

        // Copy the stream to the response's output stream.
        try {
            FileInputStream in = new FileInputStream(file);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
            in.close();
            response.getOutputStream().close();
        } catch (IOException e) {
            return -1;
        }
        return 0;
    }

    private String getOriginalFileName(String filename) {
        if (filename == null) {
            return "";
        } else {
            int unixSep = filename.lastIndexOf("/");
            int winSep = filename.lastIndexOf("\\");
            int pos = winSep > unixSep ? winSep : unixSep;
            return pos != -1 ? filename.substring(pos + 1) : filename;
        }
    }
}
