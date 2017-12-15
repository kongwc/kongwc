package com.kongwc.tools.common.util;

import com.kongwc.tools.common.exception.DevelopmentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件操作工具类
 */
@Slf4j
public class FileUtil {

    /**
     * 目录路径补全
     *
     * @param path 目录路径
     * @return 补全后目录路径
     */
    public static String patchDirPath(String path) {

        String patchPath = path;
        if (!StringUtils.isEmpty(patchPath)
                && !patchPath.endsWith(File.separator)) {
            patchPath += File.separator;
        }
        return patchPath;
    }

    /**
     * 将文件load为Byte数组
     *
     * @param filename 文件名
     * @return byte数组
     */
    public static byte[] toByteArray(String filename) {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            throw new DevelopmentException(e);
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                throw new DevelopmentException(e);
            }
        }
    }

    /**
     * 将文件load为Base64字符串
     *
     * @param filename 文件名
     * @return Base64字符串
     */
    public static String toBase64(String filename) {

        byte[] bytes = toByteArray(filename);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    /**
     * 将文件load为Base64字符串
     *
     * @param inputStream 文件流
     * @return Base64字符串
     */
    public static String toBase64(InputStream inputStream) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileUtil.toFile(inputStream, baos);
        return Base64.encodeBase64String(baos.toByteArray());
    }

    /**
     * 将Base64字符串转换为文件输入流
     *
     * @param base64 Base64字符串
     * @return 文件输入流
     */
    public static InputStream fromBase64(String base64) {

        return new ByteArrayInputStream(Base64.decodeBase64(base64));
    }


    /**
     * 将文件流保存文件
     *
     * @param inputStream 文件流
     * @param targetPath  保存路径
     */
    public static void toFile(InputStream inputStream, String targetPath) {
//        byte[] buffer = new byte[2048];
//        int len;
//        FileOutputStream outputStream = null;
//        try {
//            outputStream = new FileOutputStream(new File(targetPath));
//            while ((len = inputStream.read(buffer)) > -1) {
//                outputStream.write(buffer, 0, len);
//            }
//            outputStream.flush();
//        } catch (IOException e) {
//            throw new DevelopmentException(e);
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    throw new DevelopmentException(e);
//                }
//            }
//        }
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(targetPath));
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 将文件流保存文件
     *
     * @param inputStream  输入文件流
     * @param outputStream 保存文件流
     */
    public static void toFile(InputStream inputStream, OutputStream outputStream) {
//        byte[] buffer = new byte[2048];
//        int len;
//        FileOutputStream outputStream = null;
//        try {
//            outputStream = new FileOutputStream(new File(targetPath));
//            while ((len = inputStream.read(buffer)) > -1) {
//                outputStream.write(buffer, 0, len);
//            }
//            outputStream.flush();
//        } catch (IOException e) {
//            throw new DevelopmentException(e);
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    throw new DevelopmentException(e);
//                }
//            }
//        }
        try {
            byte[] buffer = new byte[4096];
            int len; //读取长度
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush(); //刷新缓冲的输出流
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 获取文件流
     *
     * @param targetPath 文件路径
     */
    public static InputStream fromFile(String targetPath) {
        try {
            return new FileInputStream(targetPath);
        } catch (FileNotFoundException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 获取文件流
     *
     * @param targetPath 文件路径
     */
    public static InputStream fromAutoDeleteFile(String targetPath) {
        try {
            return new DeleteOnCloseFileInputStream(targetPath);
        } catch (FileNotFoundException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 删除文件
     *
     * @param targetPath 文件路径
     */
    public static boolean deleteFile(String targetPath) {

        return FileUtils.deleteQuietly(new File(targetPath));
    }

    /**
     * 获取文件名
     *
     * @param fullPath 文件路径
     * @return 文件名
     */
    public static String getFileName(String fullPath) {

        return new File(fullPath).getName();
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 后缀名
     */
    public static String getFileExtName(String fileName) {

        int pos = fileName.lastIndexOf(".");
        if (pos < 0) {
            return null;
        }
        return fileName.substring(pos + 1);
    }

    private static class DeleteOnCloseFileInputStream extends FileInputStream {
        private File file;

        public DeleteOnCloseFileInputStream(String fileName) throws FileNotFoundException {
            this(new File(fileName));
        }

        public DeleteOnCloseFileInputStream(File file) throws FileNotFoundException {
            super(file);
            this.file = file;
        }

        public void close() throws IOException {
            try {
                super.close();
            } finally {
                if (file != null) {
                    ThreadUtil.executeOnDelay(5000, () ->{
                        boolean ret = file.delete();
                        log.debug("自动删除-----------------{} {}", file.getPath(), ret);
                        file = null;
                    });
                }
            }
        }
    }
}
