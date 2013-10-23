/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bms.common.AppContext;
import com.bms.security.core.SecurityMetadataSource;

/**
 * @author wangjian
 * @create 2013年7月30日 上午10:55:12
 * @update TODO
 * 
 * 
 */
public class SecuritySupport {
	
	private static Logger log = LoggerFactory.getLogger(SecuritySupport.class);
	
	/**
	 * @param requestURI a <code>String<code> specifying the request URI
	 * @param roleId a <code>Integer</code> specifying the role id
	 * @return true,  if the resource is public or the role has the needed authority 
	 * 		   false, if the resource is private and the role doesn't have needed authority
	 */
	public static boolean isAllowed(String requestURI, Integer roleId) {
		SecurityMetadataSource securityMetadataSource = 
				(SecurityMetadataSource) AppContext.getBean("securityMetadataSource");
		List<Integer> roleList = securityMetadataSource.getNeededRoles(requestURI);
		
		// this resource is public resource
		if(roleList == null || roleList.size() == 0) {
			log.info("haven't assigned roles who can access this resource:"+requestURI+", please check");
			return true;
		}
		// this roleId can access this resource
		if(roleList.contains(roleId)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * check whether this resource is public
	 * 
	 */
	public static boolean isPublic(String requestURI) {
		SecurityMetadataSource securityMetadataSource = 
				(SecurityMetadataSource) AppContext.getBean("securityMetadataSource");
		
		return securityMetadataSource.isPublic(requestURI);
	}
	
	/**
	 * invoked when RBAC configuration changed
	 */
	public static void reloadSecurityMetadataSource() {
		SecurityMetadataSource securityMetadataSource = 
				(SecurityMetadataSource) AppContext.getBean("securityMetadataSource");
		securityMetadataSource.reloadDefinedResource();
	}
}
