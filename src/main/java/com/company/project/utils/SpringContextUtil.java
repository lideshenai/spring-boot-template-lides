package com.company.project.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;



public class SpringContextUtil implements ApplicationContextAware {
    
    private final static Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);
	
	// Spring应用上下文环境  
    private static ApplicationContext applicationContext;  
    
    /** 
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境 
     *  
     * @param applicationContext 
     */  
    public void setApplicationContext(ApplicationContext applicationContext) {  
        SpringContextUtil.applicationContext = applicationContext;  
    }
    
    /** 
     * @return ApplicationContext 
     */  
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }
    
    /** 
     * 获取对象 
     *  
     * @param name 
     * @return Object
     * @throws BeansException 
     */  
    public static Object getBean(String name) {  
    	try {
    		return applicationContext.getBean(name);  
    	} catch(BeansException e) {
    	    logger.debug(e.toString());
    		return null;
    	}
    } 
}
