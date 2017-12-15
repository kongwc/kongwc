package com.kongwc.tools.common.data;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * API 参数父类
 */
@Data
public class ApiParameter extends WebServiceParameter {

    public static final String PARAM_KEY_MULTIPART_FILE = "paramMultipartFiles";
    public static final String PARAM_KEY_HTTP_RESPONSE = "paramHttpServletResponse";
    public static final String PARAM_KEY_REFERENCE_PATH = "paramReferencePath";

    private List<MultipartFile> paramMultipartFiles;
    private HttpServletResponse paramHttpServletResponse;
    private String paramReferencePath;
    private String ipAddr;
}
