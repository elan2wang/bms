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
import com.bms.finance.persistence.InvoiceTitleMapper;
import com.bms.finance.pojo.InvoiceTitle;

/**
 * @author asus
 * @create 2013年9月22日 下午12:29:15
 * @update TODO
 * 
 * 
 */
@Service
public class InvoiceTitleService {

	@Autowired
	private InvoiceTitleMapper invoiceTitleMapper;
	
	@Transactional
	public void insertInvoiceTitle(InvoiceTitle invoiceTitle) {
		invoiceTitleMapper.insertInvoiceTitle(invoiceTitle);
	}
	
	@Transactional
	public void updateInvoiceTitle(InvoiceTitle invoiceTitle) {
		invoiceTitleMapper.updateInvoiceTitle(invoiceTitle);
	}
	
	@Transactional
	public void deleteInvoiceTitle(Integer id) {
		invoiceTitleMapper.deleteInvoiceTitle(id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InvoiceTitle queryInvoiceTitle(Integer id) {
		Map params = new HashMap();
		params.put("id", id);
		
		return invoiceTitleMapper.queryInvoiceTitle(params);
	}
	
	public List<InvoiceTitle> queryInvoiceTitles() {
		return invoiceTitleMapper.queryInvoiceTitles();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Integer countList(String title, Boolean enable){
		Map params = new HashMap();
		params.put("title", title);
		params.put("enable", enable);
		
		return invoiceTitleMapper.countList(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<InvoiceTitle> getList(String title, Boolean enable, Page page){
		Map params = new HashMap();
		params.put("title", title);
		params.put("enable", enable);
		params.put("page", page);
		
		return invoiceTitleMapper.getList(params);
	}

}
