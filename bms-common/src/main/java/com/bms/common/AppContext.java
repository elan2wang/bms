/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author wangjian
 * @create 2013年7月30日 上午11:10:52
 * @update TODO
 * 
 * 
 */
public class AppContext implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	    
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		AppContext.applicationContext = arg0;
	}
	
}
