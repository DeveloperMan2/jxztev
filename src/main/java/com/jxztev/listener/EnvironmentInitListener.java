package com.jxztev.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *捕获spring配置文件
 */
public class EnvironmentInitListener implements ServletContextListener {

	/**
	 * applicationContext
	 */
    private static ApplicationContext applicationContext;
    
    /**
     */
    public EnvironmentInitListener() {
        super();
    }

    /**
     * @param event ServletContextEvent
     */
    @Override
	public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext));
    }
    
    /**
     * @param event ServletContextEvent
     */
    @Override
	public void contextDestroyed(ServletContextEvent event) {
        setApplicationContext(null);
    }

    /**
	 * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param applicationContext applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        EnvironmentInitListener.applicationContext = applicationContext;
    }

    /**
     * @param beanId beanId
     * @return Object
     */
    public static Object getTarget(String beanId) {
        return applicationContext.getBean(beanId);
    }
}
