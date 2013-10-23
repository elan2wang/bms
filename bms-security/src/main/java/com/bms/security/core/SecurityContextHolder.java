/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

/**
 * @author wangjian
 * @create 2013年8月1日 上午10:29:50
 * @update TODO
 * 
 * 
 */
public class SecurityContextHolder {

	private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<SecurityContext>();
	private static int initializeCount = 0;
	
	static {
		initialize();
	}
	
	private static void initialize() {
		initializeCount++;
	}

	public static void clearContext() {
        contextHolder.remove();
    }
	
	public static int getInitializeCount() {
        return initializeCount;
    }
		
	public static SecurityContext getContext() {
    	SecurityContext ctx = contextHolder.get();
    	
    	if(ctx == null) {
    		ctx = new SecurityContext();
    		contextHolder.set(ctx);
    	}
    	
    	return ctx;
    }

	public static SecurityContext createEmptyContext() {
		return new SecurityContext();
    }

	public static void setContext(SecurityContext securityContext) {
		contextHolder.set(securityContext);
	}
}
