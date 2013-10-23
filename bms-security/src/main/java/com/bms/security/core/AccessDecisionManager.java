/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bms.security.SecuritySupport;
import com.bms.security.exception.AccessDeniedException;

/**
 * @author wangjian
 * @create 2013年8月1日 下午7:42:14
 * @update TODO
 * 
 * 
 */
@Component
public class AccessDecisionManager {

	public void decide(AuthenticationToken token, String resource) throws AccessDeniedException {
		List<Integer> ownedRoles = token.getRoles();
		for (Integer roleId : ownedRoles) {
			if (SecuritySupport.isAllowed(resource, roleId)) return;
		}
		
		throw new AccessDeniedException("you can't access this resource");
	}
}
