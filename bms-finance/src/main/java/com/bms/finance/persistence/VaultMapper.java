/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance.persistence;

import java.util.List;
import java.util.Map;

import com.bms.finance.pojo.Vault;

/**
 * @author AI
 * @create 2013-9-18 下午4:17:13
 * @update TODO
 * 
 * 
 */
public interface VaultMapper {
	
	//插入一条Vault
	public void insertVault(Vault vault);
	
	//修改一条Vault
	public void updateVault(Vault vault);
	
	//删除一条Vault
	public void deleteVault(String v_number);

	//按v_number返回一条Vault
	@SuppressWarnings("rawtypes")
	public Vault queryVault(Map params);
	
	//按条件返回多条Vaults
	@SuppressWarnings("rawtypes")
	public List<Vault> queryVaults(Map params);
	
	//按条件返回Vaults的数目
	@SuppressWarnings("rawtypes")
	public Integer getVaultsCount(Map params);
	
	//获取当前可分配的账户
	public List<Vault> getAvailableVaults();
	
	//按部门编号获取正在使用的账户
	@SuppressWarnings("rawtypes")
	public List<Vault> getCurrentVaults(Map params);
	
	//禁用启用账户
	@SuppressWarnings("rawtypes")
	public void switchVault_enable(Map params);
	
	//增加账户的出账次数
	@SuppressWarnings("rawtypes")
	public void addPaymentCounts(Map params);
	
	//增加账户的入账次数
	@SuppressWarnings("rawtypes")
	public void addIncomeCounts(Map params);
}
