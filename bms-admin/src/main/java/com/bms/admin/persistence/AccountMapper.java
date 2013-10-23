/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.Account;

/**
 * @author wangjian
 * @create 2013年8月9日 上午10:52:30
 * @update TODO
 * 
 * 
 */
public interface AccountMapper {

	public void insertAccount(Account account);
	
	public void updateAccount(Account account);
	
	public void deleteAccount(Integer account_id);
	
	@SuppressWarnings("rawtypes")
	public Account queryAccount(Map params);
		
	@SuppressWarnings("rawtypes")
	public List<Account> queryAccounts(Map params);
	
	///根据部门编号获取相关出纳账号
	public List<Account> queryAccountsByDepartmentId(Integer dep_id);
	
}
