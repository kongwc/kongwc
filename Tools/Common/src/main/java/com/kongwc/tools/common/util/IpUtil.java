package com.kongwc.tools.common.util;

import com.kongwc.tools.common.exception.DevelopmentException;
import org.apache.commons.lang.SystemUtils;
import sun.net.util.IPAddressUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络相关工具函数
 */
public abstract class IpUtil {

    /**
     * 判断网络是否正常
     *
     * @param ipAddr IP地址
     * @return 是否正常
     */
    public static int isIpReachable(String ipAddr) {

//        try {
//            InetAddress address = InetAddress.getByName(ipAddr);
//            boolean reachable = address.isReachable(5000);
//            if (reachable) {
//                return 1;
//            }
//            return 2;
//        } catch (IOException e) {
//            return 0;
//        }

        List<String> command = new ArrayList<>();
        command.add("ping");

        if (SystemUtils.IS_OS_WINDOWS) {
            command.add("-n");
        } else if (SystemUtils.IS_OS_UNIX) {
            command.add("-c");
        } else {
            throw new UnsupportedOperationException("Unsupported operating system");
        }
        command.add("1");
        command.add(ipAddr);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process;
        try {
            process = processBuilder.start();
            BufferedReader standardOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String outputLine;
            while ((outputLine = standardOutput.readLine()) != null) {
                // Picks up Windows and Unix unreachable hosts
                if (outputLine.toLowerCase().contains("ttl")) {
                    return 1;
                }
            }
        } catch (IOException e) {
            return 0;
        }
        return 2;
    }

    /**
     * 获取本机IP地址
     *
     * @return IP地址
     */
    public static String getLocalIp() {

        String ip;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            try {
                ip = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e1) {
                throw new DevelopmentException(e1);
            }
        }
        return ip;
    }

    /**
     * 根据域名获取IP地址
     *
     * @param ipAddr 域名
     * @return IP地址
     */
    public static String getIpByName(String ipAddr) {

        String ip = ipAddr;
        if (!IPAddressUtil.isIPv4LiteralAddress(ipAddr)) {
            try {
                ip = InetAddress.getByName(ipAddr).getHostAddress();
            } catch (UnknownHostException e) {
                throw new DevelopmentException(e);
            }
        }
        return ip;
    }
}
