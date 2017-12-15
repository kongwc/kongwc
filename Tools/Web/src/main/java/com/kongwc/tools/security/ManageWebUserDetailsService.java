package com.kongwc.tools.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhanghx on 2017/09/22.
 */
@Service("DeviceWebUserDetailsImpl")
@PropertySource(value = "classpath:/common.properties", ignoreResourceNotFound = true)
public class ManageWebUserDetailsService implements UserDetailsService {

    @Value("${CONSTANT_PASSWORD}")
    private String password;
    @Value("${CONSTANT_USER}")
    private String username;
    @Value("${CONSTANT_SELF_PATH}")
    private String propertiesPath;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        if (!userName.equals(this.username)) {
            throw new UsernameNotFoundException("user select fail");
        }
        return new ManageWebUserDetails(userName, this.password);
    }

    private void read(){
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("/common.properties");
            Properties p = new Properties();
            p.load(in);

            System.out.println(p.getProperty("ip")+","+p.getProperty("username")+","+p.getProperty("pwd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean chgUserPassword(String password){
        try{
            File file = ResourceUtils.getFile("classpath:common.properties");
            Properties p = new Properties();
            FileInputStream in = new FileInputStream(file);
            p.load(in);
            FileOutputStream out = new FileOutputStream(file);

            p.setProperty("CONSTANT_PASSWORD", password);
            p.store(out,"password update");
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
