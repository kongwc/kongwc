package com.kongwc.tools.common.util;

import com.alibaba.druid.sql.dialect.db2.visitor.DB2EvalVisitor;
import com.kongwc.tools.common.exception.DevelopmentException;
import com.kongwc.tools.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * 图片相关工具类
 */
@Slf4j
public class ImageUtil {

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code 文件内容
     * @param targetPath 保存路径
     */
    public static void decodeBase64File(String base64Code, String targetPath) {
        File file = new File(targetPath);

        if (file.exists()) {
            file.delete();
        }
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try {
            if (!file.createNewFile()) {
                throw new SystemException(String.format("File create failed:%s", targetPath));
            } else {
                byte[] buffer = new Base64().decode(base64Code);
                FileOutputStream out = new FileOutputStream(targetPath);
                out.write(buffer);
                out.close();
                log.debug("文件保存成功！:{}", targetPath);
            }
        } catch (IOException e) {
            throw new SystemException(String.format("File create failed:%s", targetPath), e);
        }
    }

    /**
     * 将base64字符解码
     *
     * @param base64Code 将base64字符内容
     * @return 解码后的内容
     */
    public static byte[] decodeBase64(String base64Code) {
        return Base64.decodeBase64(base64Code);
    }

    /**
     * 转化为Base64字符
     *
     * @param bytes 转化对象
     * @return 转化后的内容
     */
    public static String encodeBase64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 从指定位置截取图片
     * @param fileBase64 输入图片
     * @param left 左
     * @param top 顶部
     * @param right 右
     * @param bottom 底部
     * @param formatName 图片格式
     * @return 截取结果图片
     */
    public static String cutImage(String fileBase64, int left, int top, int right, int bottom, String formatName) {

        try {

            /*
             * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader
             * 声称能够解码指定格式。 参数：formatName - 包含非正式格式名称 .
             *（例如 "jpeg" 或 "tiff"）等 。
             */
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(formatName);
            ImageReader reader = it.next();

            //获取图片流
            InputStream is = FileUtil.fromBase64(fileBase64);
            ImageInputStream iis = ImageIO.createImageInputStream(is);

            /*
             * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader
             * 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis, true);

           /*
             * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件
             * 将从其 ImageReader 实现的 getDefaultReadParam 方法中返回
             * ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();

             /*
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            Rectangle rect = new Rectangle(left, top, right, bottom);


            //提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);

             /*
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将
             * 它作为一个完整的 BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0, param);

            //保存新图片
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bi, formatName, bos);

            return Base64.encodeBase64String(bos.toByteArray());

        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }
}
