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
import com.bms.finance.persistence.IncomeMapper;
import com.bms.finance.pojo.Income;

/**
 * @author asus
 * @create 2013年8月15日 下午1:27:11
 * @update TODO
 * 
 * 
 */
@Service
public class IncomeService {

	@Autowired
	private IncomeMapper incomeMapper;
	
	@Transactional
	public void insertIncome(Income income) {
		incomeMapper.insertIncome(income);
	}
	
	@Transactional
	public void updateIncome(Income income) {
		incomeMapper.updateIncome(income);
	}
	
	@Transactional
	public void deleteIncome(Integer id) {
		incomeMapper.deleteIncome(id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Income queryIncome(Integer id) {
		Map params = new HashMap();
		params.put("id", id);
		
		return incomeMapper.queryIncome(params);
	}
	
	@Transactional
	public void accountChecking(Income income) {
		incomeMapper.updateIncome(income);
	}
	
	@Transactional
	public void handleException(Income income) {
		incomeMapper.updateIncome(income);
	}
	
	@Transactional
	public void invoice(Income income) {
		incomeMapper.updateIncome(income);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer countMyApplyList(Integer creator_id, Integer id, String title, Integer income_type,
			String bring_type, String invoice_type, String state, String invoice_state){
		Map params = new HashMap();
		params.put("creator_id", creator_id);
		params.put("id", id);
		params.put("title", title);
		params.put("income_type", income_type);
		params.put("bring_type", bring_type);
		params.put("invoice_type", invoice_type);
		params.put("state", state);
		params.put("invoice_state", invoice_state);
		
		return incomeMapper.countMyApplyList(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Income> getMyApplyList(Integer creator_id, Integer id, String title, Integer income_type,
			String bring_type, String invoice_type, String state, String invoice_state, String order_type, Page page){
		Map params = new HashMap();
		params.put("creator_id", creator_id);
		params.put("id", id);
		params.put("title", title);
		params.put("income_type", income_type);
		params.put("bring_type", bring_type);
		params.put("invoice_type", invoice_type);
		params.put("state", state);
		params.put("invoice_state", invoice_state);
		params.put("order_type", order_type);
		params.put("page", page);
		
		return incomeMapper.getMyApplyList(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer countAllIncomes(List<Integer> ids, Integer income_type, String bring_type, 
			String invoice_type, String state, String invoice_state, Integer department_id){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("income_type", income_type);
		params.put("bring_type", bring_type);
		params.put("invoice_type", invoice_type);
		params.put("state", state);
		params.put("invoice_state", invoice_state);
		params.put("department_id", department_id);
		
		return incomeMapper.countAllIncomes(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Income> getAllIncomes(List<Integer> ids, Integer income_type, String bring_type, 
			String invoice_type, String state, String invoice_state, Integer department_id, String order_type, Page page){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("income_type", income_type);
		params.put("bring_type", bring_type);
		params.put("invoice_type", invoice_type);
		params.put("state", state);
		params.put("invoice_state", invoice_state);
		params.put("department_id", department_id);
		params.put("order_type", order_type);
		params.put("page", page);
		
		return incomeMapper.getAllIncomes(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer countAccountCheckingList(List<Integer> ids, Integer id, String bring_type, 
			String invoice_type, String invoice_state, Integer department_id){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("bring_type", bring_type);
		params.put("invoice_type", invoice_type);
		params.put("invoice_state", invoice_state);
		params.put("department_id", department_id);
		
		return incomeMapper.countAccountCheckingList(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Income> getAccountCheckingList(List<Integer> ids, Integer id, String bring_type, 
			String invoice_type, String invoice_state, Integer department_id, String order_type, Page page){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("bring_type", bring_type);
		params.put("invoice_type", invoice_type);
		params.put("invoice_state", invoice_state);
		params.put("department_id", department_id);
		params.put("order_type", order_type);
		params.put("page", page);
		
		return incomeMapper.getAccountCheckingList(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer countInvoiceList(List<Integer> ids, Integer id, String invoice_type, 
			String state, Integer department_id){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("invoice_type", invoice_type);
		params.put("state", state);
		params.put("department_id", department_id);
		
		return incomeMapper.countInvoiceList(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Income> getInvoiceList(List<Integer> ids, Integer id, String invoice_type, 
			String state, Integer department_id, String order_type, Page page){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("invoice_type", invoice_type);
		params.put("state", state);
		params.put("department_id", department_id);
		params.put("order_type", order_type);
		params.put("page", page);
		
		return incomeMapper.getInvoiceList(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer countPendingList(Integer income_type, String bring_type, String invoice_type, 
			String invoice_state, Integer department_id){
		Map params = new HashMap();
		params.put("income_type", income_type);
		params.put("bring_type", bring_type);
		params.put("invoice_type", invoice_type);
		params.put("invoice_state", invoice_state);
		params.put("department_id", department_id);
		
		return incomeMapper.countPendingList(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Income> getPendingList(Integer income_type, String bring_type, String invoice_type, 
			String invoice_state, Integer department_id, String order_type, Page page){
		Map params = new HashMap();
		params.put("income_type", income_type);
		params.put("bring_type", bring_type);
		params.put("invoice_type", invoice_type);
		params.put("invoice_state", invoice_state);
		params.put("department_id", department_id);
		params.put("order_type", order_type);
		params.put("page", page);
		
		return incomeMapper.getPendingList(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Integer> creatorToOthers(String role_name, Integer dep_id){
		Map params = new HashMap();
		params.put("role_name", role_name);
		params.put("dep_id", dep_id);
		
		return incomeMapper.creatorToOthers(params);
	}
	
}
