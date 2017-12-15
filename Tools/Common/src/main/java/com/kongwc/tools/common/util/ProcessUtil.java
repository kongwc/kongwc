package com.kongwc.tools.common.util;

import com.kongwc.tools.common.exception.DevelopmentException;
import com.sun.jna.Pointer;
import de.flapdoodle.embed.process.runtime.ProcessControl;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 命令行模式工具函数
 */
@Slf4j
public abstract class ProcessUtil {

    /**
     * 匹配NETSTAT命令结果的正则表达式
     */
    private static final Pattern PATTERN_NETSTAT_PORT = Pattern.compile("TCP\\s+.+?:(\\d+?)\\s+.*LISTENING.*");

    /**
     * 启动进程
     *
     * @param cmd 命令
     * @return 进程ID
     */
    public static String startProcess(String[] cmd) {

        ProcessBuilder processBuilder = new ProcessBuilder().command(cmd);
//        processBuilder.redirectError(new File("D:\\test.txt"));
        try {
            ProcessControl control = ProcessControl.start(null, processBuilder);
            return String.valueOf(control.getPid());
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 根据进程ID获取该进程占用的端口号
     * @param pid 进程ID
     * @return 端口号
     */
    public static String getProcessListeningPort(String pid) {

        int count = 0;
        String port;
        while(count < 10) {
            port = getProcessPortCore(pid);
            if (port != null) {
                return port;
            }
            count ++;
            org.apache.hadoop.util.ThreadUtil.sleepAtLeastIgnoreInterrupts(2000);
        }
        return null;
    }

    /**
     * 根据进程ID获取该进程占用的端口号
     * @param pid 进程ID
     * @return 端口号
     */
    private static String getProcessPortCore(String pid) {

        try {
            String cmd = "cmd /c NETSTAT -ano | findstr ".concat(pid);
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //  "TCP    0.0.0.0:19819          0.0.0.0:0              LISTENING       3388"
            log.debug("-------------------{}-------------------------", pid);
            while ((line = br.readLine()) != null) {
                log.debug(line);
                if (line.endsWith(" ".concat(pid))) {
                    Matcher m = PATTERN_NETSTAT_PORT.matcher(line);
                    if (m.find()) {
                        return m.group(1);
                    }
                }
            }
            log.debug("--------------------------------------------");
            return null;
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 根据进程ID停止进程
     *
     * @param pid 进程ID
     */
    public static void killProcessByPid(String pid) {

        String[] cmd = new String[]{"TASKKILL", "/F", "/PID", pid};
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }

    /**
     * 获取指定进程名的进程PID
     *
     * @param processName 进程名
     * @return 进程PID
     */
    public static List<String> getAllProcessPid(String processName) {

        try {
            String[] cmd = new String[]{"TASKLIST", "/FI", "IMAGENAME eq ".concat(processName), "/NH", "/FO", "CSV"};
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<String> pids = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String pid = line.split(",")[1];
                if (pid.startsWith("\"")) {
                    pid = pid.substring(1);
                }
                if (pid.endsWith("\"")) {
                    pid = pid.substring(0, pid.length() - 1);
                }
                pids.add(pid);
            }
            return pids;
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }
}
