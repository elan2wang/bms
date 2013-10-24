/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.common.Page;
import com.bms.finance.persistence.VaultMapper;
import com.bms.finance.pojo.Vault;

/**
 * @author AI
 * @create 2013-9-18 下午5:07:19
 * @update TODO
 * 
 * 
 */
@Service
public class VaultService {

	@Autowired
	private VaultMapper vaultMapper;
	
	@Transactional
	public void insertVault(Vault vault){
		vaultMapper.insertVault(vault);
	}
	
	@Transactional
	public void updateVault(Vault vault){
		vaultMapper.updateVault(vault);
	}
	
	@Transactional
	public void deleteVault(String v_number){
		vaultMapper.deleteVault(v_number);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Vault queryVaultByV_number(String v_number){
		Map params = new HashMap();
		params.put("v_number", v_number);
		return vaultMapper.queryVault(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Vault> queryVaults(String v_number, String type,
			String alias, String card_bank, String card_owner,
			String sort, String order, Page page){
		Map params = new HashMap();
		params.put("v_number", v_number);
		params.put("type", type);
		params.put("alias", alias);
		params.put("card_bank", card_bank);
		params.put("card_owner", card_owner);
		params.put("sort", sort);
		params.put("order", order);
		params.put("startIndex", page.getStartIndex() - 1);
		params.put("currentItemCount", page.getCurrentItemCount());
		return vaultMapper.queryVaults(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer getVaultCounts(String v_number, String type,
			String alias, String card_bank, String card_owner){
		Map params = new HashMap();
		params.put("v_number", v_number);
		params.put("type", type);
		params.put("alias", alias);
		params.put("card_bank", card_bank);
		params.put("card_owner", card_owner);
		return vaultMapper.getVaultsCount(params);
	}
	
	public List<Vault> getAvailableVaults(){
		return vaultMapper.getAvailableVaults();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Vault> getCurrentVaultsByDep_id(Integer dep_id){
		Map params = new HashMap();
		params.put("dep_id", dep_id);
		return vaultMapper.getCurrentVaults(params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setVault_enable(String v_number, Boolean vault_enable){
		Map params = new HashMap();
		params.put("v_number", v_number);
		params.put("vault_enable", vault_enable);
		vaultMapper.switchVault_enable(params);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addCounts(String v_number, String type){
		Map params = new HashMap();
		params.put("v_number", v_number);
		if(type.equals("payment")){
			vaultMapper.addPaymentCounts(params);
		}else{
			vaultMapper.addIncomeCounts(params);
		}
	}
}
