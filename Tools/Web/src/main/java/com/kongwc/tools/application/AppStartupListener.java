package com.kongwc.tools.application;

import com.alibaba.fastjson.util.TypeUtils;
import com.kongwc.tools.common.application.AppContextSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 系统启动事件监听Class
 */
@Slf4j
public class AppStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        log.info("onApplicationEvent {}", event.getSource());
        ApplicationContext context = event.getApplicationContext();
        onAppInit(context);

        // Application上下文管理类初始化
        AppContextSupport acs = context.getBean(AppContextSupport.class);
        acs.setApplicationContext(context);
    }

    /**
     * 应用初始化处理
     *
     * @param context 应用上下文
     */
    public void onAppInit(ApplicationContext context) {
        TypeUtils.compatibleWithJavaBean = true;
        TypeUtils.compatibleWithFieldName = true;
    }

}
