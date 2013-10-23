/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance.persistence;

import java.util.List;
import java.util.Map;

import com.bms.finance.pojo.InvoiceTitle;

/**
 * @author asus
 * @create 2013年9月22日 下午12:21:24
 * @update TODO
 * 
 * 
 */
public interface InvoiceTitleMapper {
	
	public void insertInvoiceTitle(InvoiceTitle invoiceTitle);
	
	public void updateInvoiceTitle(InvoiceTitle invoiceTitle);
	
	public void deleteInvoiceTitle(Integer id);
	
	@SuppressWarnings("rawtypes")
	public InvoiceTitle queryInvoiceTitle(Map params);
	
	public List<InvoiceTitle> queryInvoiceTitles();
	
	@SuppressWarnings("rawtypes")
	public Integer countList(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<InvoiceTitle> getList(Map params);

}
