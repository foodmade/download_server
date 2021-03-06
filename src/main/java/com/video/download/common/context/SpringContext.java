package com.video.download.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Spring context get from the IOC container bean example
 */
@Component
@Lazy(false)
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    /**
     * Fetch applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    /**
     * Get bean from IOC container by bean name
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
    /**
     * Get bean from IOC container by bean class
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
