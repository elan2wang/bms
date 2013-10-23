/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.admin.persistence.AccountMapper;
import com.bms.admin.pojo.Account;
import com.bms.common.BaseService;
import com.bms.common.Page;

/**
 * @author wangjian
 * @create 2013年8月9日 上午11:10:48
 * @update TODO
 * 
 * 
 */
@Service
public class AccountService extends BaseService{
	
	@Autowired
	private AccountMapper accountMapper;
	
	@Transactional
	public void insertAccount(Account account) {
		accountMapper.insertAccount(account);
	}
	
	@Transactional
	public void updateAccount(Account account) {
		accountMapper.updateAccount(account);
	}
	
	@Transactional
	public void deleteAccount(Integer account_id) {
		accountMapper.deleteAccount(account_id);
	}
	
	public Account queryAccountByUsername(String username) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		
		return accountMapper.queryAccount(params);
	}
	
	public Account queryAccountById(Integer account_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_id", account_id);
		return accountMapper.queryAccount(params);
	}

	public Account queryAccountByMobile(String mobile) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", mobile);
		
		return accountMapper.queryAccount(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Account> queryAccounts(Map params) {
		List<Account> list = accountMapper.queryAccounts(params);
		Page page = (Page) params.get("page");
		return paging(list, page);
	}
	
	//根据部门编号获取相关出纳账号
	public List<Account> queryAccountsByDepartmentId(Integer dep_id) {
		return accountMapper.queryAccountsByDepartmentId(dep_id);
	}
	
}
