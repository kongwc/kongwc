package com.kongwc.tools;

import com.kongwc.tools.application.AppStartupListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * 系统入口Class
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.kongwc.tools.common", "com.kongwc.tools.web"})
public class ToolMainRoot extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ToolMainRoot.class);
    }

    /**
     * 主启动函数
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(ToolMainRoot.class);

        // 注册启动事件监听listener
        springApplication.addListeners(new AppStartupListener());

        // spring boot启动
        springApplication.run(args);
    }
}
