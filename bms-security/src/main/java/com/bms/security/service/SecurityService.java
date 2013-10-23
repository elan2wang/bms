/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bms.security.core.Account;
import com.bms.security.persistence.SecurityMapper;

/**
 * @author wangjian
 * @create 2013年7月30日 上午10:50:49
 * @update TODO
 * 
 * 
 */
@Service
public class SecurityService {
	
	@Autowired
	private SecurityMapper securityMapper;
	
	public List<String> queryPublicResources() {
		return securityMapper.queryPublicResources();
	}

	public List<String> queryNonPublicResources(){
		return securityMapper.queryNonPublicResources();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Account queryAccountByUsername(String username){
		Map params = new HashMap();
		params.put("username", username);
		return securityMapper.queryAccountByUsername(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Integer> queryAccountRoles(Integer account_id){
		Map params = new HashMap();
		params.put("account_id", account_id);
		return securityMapper.queryAccountRoles(params);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Integer> queryNeededRoles(String resource){
		Map params = new HashMap();
		params.put("res_link", resource);
		return securityMapper.queryNeededRoles(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateLastLoginInfo(String username, String ipAddress) {
		Map params = new HashMap();
		params.put("username", username);
		params.put("last_login_ip", ipAddress);
		params.put("last_login_time", new Timestamp(System.currentTimeMillis()));
		securityMapper.updateLastLoginInfo(params);
	}
}
