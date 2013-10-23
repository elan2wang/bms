/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance.persistence;

import java.util.List;
import java.util.Map;

import com.bms.finance.pojo.Income;

/**
 * @author asus
 * @create 2013年8月14日 下午9:13:26
 * @update TODO
 * 
 * 
 */
public interface IncomeMapper {
	
	public void insertIncome(Income income);
	
	public void updateIncome(Income income);
	
	public void deleteIncome(Integer id);
	
	@SuppressWarnings("rawtypes")
	public Income queryIncome(Map params);
	
	@SuppressWarnings("rawtypes")
	public Integer countMyApplyList(Map params);
		
	@SuppressWarnings("rawtypes")
	public List<Income> getMyApplyList(Map params);
	
	@SuppressWarnings("rawtypes")
	public Integer countAllIncomes(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Income> getAllIncomes(Map params);
	
	@SuppressWarnings("rawtypes")
	public Integer countAccountCheckingList(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Income> getAccountCheckingList(Map params);
	
	@SuppressWarnings("rawtypes")
	public Integer countInvoiceList(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Income> getInvoiceList(Map params);
	
	@SuppressWarnings("rawtypes")
	public Integer countPendingList(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Income> getPendingList(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Integer> creatorToOthers(Map params);

}
