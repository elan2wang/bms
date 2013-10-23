/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bms.security.service.SecurityService;

/**
 * @author wangjian
 * @create 2013年7月30日 上午10:48:54
 * @update TODO
 * 
 * 
 */
@Component
public class SecurityMetadataSource {

	private static Logger log = LoggerFactory.getLogger(SecurityMetadataSource.class);
	private SecurityService securityService;

	/**
	 * key : resource link
	 * value: role_id list
	 * 
	 * store all the private resources and their needed roles
	 */
	private Map<String, List<Integer>> nonPublicResourcesMap = null;
	private List<String> publicResources = null;
	
	@Autowired
	public SecurityMetadataSource(SecurityService securityService) {
		this.securityService = securityService;
		loadDefinedResource();
	}

	/**
	 * invoked in constructor to load all the resources and their need roles
	 * and store them in the HashMap
	 * 
	 */
	private void loadDefinedResource() {
		// fill in resourceMap
		List<String> resList = securityService.queryNonPublicResources();
		nonPublicResourcesMap = new HashMap<String, List<Integer>>();
		if(resList == null) return;

		for(String resource : resList) {
			List<Integer> roleList = securityService.queryNeededRoles(resource);
			if (roleList.size() == 0) {
				log.info("haven't assigned roles who can access this resource:"+resource+", please check.");
				continue;
			}
			nonPublicResourcesMap.put(resource, roleList);
		}
		
		// fill in publicResources
		publicResources = securityService.queryPublicResources();
	}

	/**
	 * Invoked when the RBAC configuration changed
	 *
	 */
	public void reloadDefinedResource() {
		loadDefinedResource();
	}

	/**
	 * get the needed roleId list of the specific resource
	 * represented by the request URL
	 *
	 * @param url the request URL
	 * @return List<Integer> roleId list
	 */
	public List<Integer> getNeededRoles(String url) {
		String resource = null;
		if(url.indexOf("?") != -1) {
			resource = url.split("?")[0];
		} else {
			resource = url;
		}
		
		if (nonPublicResourcesMap == null) {
			return null;
		}
		return nonPublicResourcesMap.get(resource);
	}

	public boolean isPublic(String url) {
		String resource = null;
		if(url.indexOf("?") != -1) {
			resource = url.split("?")[0];
		} else {
			resource = url;
		}
		
		return publicResources.contains(resource);
	}
}