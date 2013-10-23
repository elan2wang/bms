/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance.persistence;

import java.util.List;
import java.util.Map;

import com.bms.finance.pojo.Payment;

/**
 * @author AI
 * @create 2013年8月12日 下午8:36:26
 * @update TODO
 * 
 * 
 */
public interface PaymentMapper {

	public void insertPayment(Payment payment);
	
	public void updatePayment(Payment payment);
	
	@SuppressWarnings("rawtypes")
	public void updatePayments(Map params);
	
	public void deletePayment(Integer payment_id);
	
	@SuppressWarnings("rawtypes")
	public Payment queryPayment(Map params);
		
	@SuppressWarnings("rawtypes")
	public List<Payment> queryPayments(Map params);
	
	//按条件获取申请人出账申请列表
	@SuppressWarnings("rawtypes")
	public List<Payment> getMyApplyList(Map params);
	
	//按条件获取总（区域）经理待审批列表
	@SuppressWarnings("rawtypes")
	public List<Payment> getAuditList(Map params);
		
	//按条件获取财务待出账列表
	@SuppressWarnings("rawtypes")
	public List<Payment> getAccountantPayList(Map params);
		
	//按条件获取财务待收发票列表
	@SuppressWarnings("rawtypes")
	public List<Payment> getAccountantInvoiceList(Map params);
		
	//按条件获取出纳待出账列表
	@SuppressWarnings("rawtypes")
	public List<Payment> getTellerPayList(Map params);
		
	//按条件获取所有出账申请列表
	@SuppressWarnings("rawtypes")
	public List<Payment> getAll(Map params);
	
	//按条件获取申请人出账申请数
	@SuppressWarnings("rawtypes")
	public Integer getMyApplyCount(Map params);
	
	//按条件获取总（区域）经理待审批出账申请数
	@SuppressWarnings("rawtypes")
	public Integer getAuditCount(Map params);
	
	//按条件获取财务待出账申请数
	@SuppressWarnings("rawtypes")
	public Integer getAccountantPayCount(Map params);
	
	//按条件获取财务待收发票数
	@SuppressWarnings("rawtypes")
	public Integer getAccountantInvoiceCount(Map params);
	
	//按条件获取财务待收发票数
	@SuppressWarnings("rawtypes")
	public Integer getTellerPayCount(Map params);
	
	//按条件获取所有出账申请数
	@SuppressWarnings("rawtypes")
	public Integer getAllCount(Map params);
	
	//按条件获取消息接收人
	@SuppressWarnings("rawtypes")
	public List<Integer> getMessageReceiver(Map params);
	
	//按多个编号获取出账申请
	@SuppressWarnings("rawtypes")
	public List<Payment> queryPaymentsByIds(Map params);
}
