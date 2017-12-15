package com.kongwc.tools.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * Created by zhanghx on 2017/09/22.
 */
@Component
public class ManageWebAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private ManageWebUserDetailsService userService;

    /**
     * 定义加密方式
     */
    private final static String KEY_SHA = "SHA";
    private final static String KEY_SHA1 = "SHA-1";
    /**
     * 全局数组
     */
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 自定义验证方式
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        if (!this.authenticateUser(username, password)) {
            throw new BadCredentialsException("Wrong password.");
        }
//        if(user == null){
//            throw new BadCredentialsException("Username not found.");
//        }
//
//        //加密
//        try
//        {
//            if (!SHAencrypt.encryptSHA(password).equals(user.getPassword())) {
//                throw new BadCredentialsException("Wrong password.");
//            }
//        } catch (Exception e) {
//            throw new BadCredentialsException("Wrong password.");
//        }

        ManageWebUserDetails user = (ManageWebUserDetails) userService.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    public boolean authenticateUser(String username, String password) {
        ManageWebUserDetails user = (ManageWebUserDetails) userService.loadUserByUsername(username);
        if (user == null) {
            return false;
        }

        //加密
        try {
            if (!SHAencrypt.encryptSHA(password).equals(user.getPassword())) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

    /**
     * SHA 加密
     *
     * @param data 需要加密的字符串
     * @return 加密之后的字符串
     * @throws Exception
     */
    public String encryptSHA(String data) {
        // 验证传入的字符串
        if (data == null || data.equals("")) {
            return "";
        }
        // 创建具有指定算法名称的信息摘要
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(KEY_SHA);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        // 使用指定的字节数组对摘要进行最后更新
        sha.update(data.getBytes());
        // 完成摘要计算
        byte[] bytes = sha.digest();
        // 将得到的字节数组变成字符串返回
        return byteArrayToHexString(bytes);
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteToHexString(byte b) {
        int ret = b;
        //System.out.println("ret = " + ret);
        if (ret < 0) {
            ret += 256;
        }
        int m = ret / 16;
        int n = ret % 16;
        return hexDigits[m] + hexDigits[n];
    }

    /**
     * 转换字节数组为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }
}
