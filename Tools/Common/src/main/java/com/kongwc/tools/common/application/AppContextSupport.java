package com.kongwc.tools.common.application;

import com.kongwc.tools.common.exception.DevelopmentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 应用上下文工具类
 */
@Component
@Slf4j
public class AppContextSupport implements ApplicationContextAware {

    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext 应用上下文
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        AppContextSupport.applicationContext = applicationContext;
    }

    /**
     * 获取应用上下文
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 判断对象实例是否存在
     *
     * @param name 对象bean名称
     * @return 是否存在
     */
    public static boolean containsBean(String name) {

        return applicationContext.containsBean(name);
    }

    /**
     * 获取对象实例
     *
     * @param name 对象bean名称
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, boolean byRigister, Object... args) {

        return getBean(name, byRigister, null, args);
    }

    /**
     * 获取对象实例
     *
     * @param name 对象bean名称
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, boolean byRigister, String scope, Object... args) {

        if (byRigister) {
            registerBean(name, name, scope);
        }
        log.debug("getBean {}---{}", name, applicationContext.isPrototype(name));
        return (T) applicationContext.getBean(name, args);
    }

    /**
     * 获取对象实例
     *
     * @param name 对象bean名称
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, String className, boolean byRigister, String scope, Object... args) {

        if (byRigister) {
            registerBean(name, className, scope);
        }
        log.debug("getBean {}---{}", name, applicationContext.isPrototype(name));
        return (T) applicationContext.getBean(name, args);
    }

    /**
     * 获取对象实例
     *
     * @param clazz 对象bean类型
     * @param args  参数
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz, Object... args) {
        return applicationContext.getBean(clazz, args);
    }

    /**
     * 根据类名加载类
     *
     * @param className      类名称
     * @param throwException 是否抛出异常
     * @return 类
     */
    @SuppressWarnings("unchecked")
    @Cacheable(value = "loadClass")
    public static <T> Class<T> loadClass(String className, boolean throwException) {
        try {
            return (Class<T>) applicationContext.getClassLoader().loadClass(className);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            if (throwException) {
                throw new DevelopmentException(e);
            }
            return null;
        }
    }

    /**
     * 动态注册Bean
     *
     * @param beanName  Bean名称
     * @param className 类名
     * @param scope     作用域
     */
    public static void registerBean(String beanName, String className, String scope) {

        ApplicationContext context = AppContextSupport.getApplicationContext();
        if (!context.containsBean(beanName)) {
            BeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClassName(className);
            if (!StringUtils.isEmpty(scope)) {
                beanDefinition.setScope(scope);
            }
            DefaultListableBeanFactory fty = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
            fty.registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
