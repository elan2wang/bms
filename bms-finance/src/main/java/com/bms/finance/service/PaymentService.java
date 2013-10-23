/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.common.Page;
import com.bms.finance.persistence.PaymentMapper;
import com.bms.finance.pojo.Payment;

/**
 * @author AI
 * @create 2013年8月12日 下午8:50:52
 * @update TODO
 * 
 * 
 */
@Service
public class PaymentService {

	@Autowired
	private PaymentMapper paymentMapper;
	
	@Transactional
	public void insertPayment(Payment payment) {
		paymentMapper.insertPayment(payment);	
	}
	
	@Transactional
	public void updatePayment(Payment payment) {
		paymentMapper.updatePayment(payment);
	}
	
	@Transactional
	public void deletePayment(Integer payment_id) {
		paymentMapper.deletePayment(payment_id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Payment queryPaymentById(Integer id){
		Map params = new HashMap();
		params.put("id", id);
		return paymentMapper.queryPayment(params);
	}
	
	@Transactional
	public void auditPayment(Payment payment) {
		paymentMapper.updatePayment(payment);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Transactional
	public void batchAuditPayments(List<Integer> ids, String state,
			Timestamp audit_time, Integer manager_id) {
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("state", state);
		params.put("audit_time", audit_time);
		params.put("manager_id", manager_id);
		params.put("last_modify_person",manager_id);
		params.put("last_modify_time", audit_time);
		
		paymentMapper.updatePayments(params);
	}
	
	@Transactional
	public void accountantPay(Payment payment) {
		paymentMapper.updatePayment(payment);
	}
	
	@Transactional
	public void tellerPay(Payment payment) {
		paymentMapper.updatePayment(payment);
	}
	
	@Transactional
	public void confirmInvoice(Payment payment) {
		paymentMapper.updatePayment(payment);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Payment> queryPayments(Boolean invoice_need){
		Map params = new HashMap();
		params.put("invoice_need", invoice_need);
		return paymentMapper.queryPayments(params);
	}
	
	//获取申请人出账申请列表
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Payment> getMyApplyList(Integer applicant_id, Integer id,
			String title, Boolean urgency, String pay_type,
			Boolean invoice_need, String state, String invoice_state,
			String sort, String order, Page page){
		Map params = new HashMap();
		params.put("applicant_id", applicant_id );
		params.put("id", id );
		params.put("title", title);
		params.put("urgency", urgency);
		params.put("pay_type", pay_type);
		params.put("invoice_need", invoice_need);
		params.put("state", state);
		params.put("invoice_state", invoice_state);
		params.put("startIndex", page.getStartIndex() - 1);
		params.put("currentItemCount", page.getCurrentItemCount());
		params.put("sort", sort);
		params.put("order", order);
		return paymentMapper.getMyApplyList(params);
	}
	
	//获取总（区域）经理待审批列表
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Payment> getAuditList(List<Integer> ids, Integer id,
			Integer department_id, String title, String pay_type,
			Boolean urgency, Boolean invoice_need, String sort, 
			String order, Page page){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("department_id", department_id);
		params.put("title", title);
		params.put("pay_type", pay_type);
		params.put("invoice_need", invoice_need);
		params.put("startIndex", page.getStartIndex() - 1);
		params.put("currentItemCount", page.getCurrentItemCount());
		params.put("sort", sort);
		params.put("order", order);
		return paymentMapper.getAuditList(params);
	}
	
	//获取财务待出账列表
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Payment> getAccountantPayList(List<Integer> ids, Integer id,
			Integer department_id, String title, String pay_type,
			Boolean invoice_need, Boolean urgency, 
			String sort, String order, Page page){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("department_id", department_id);
		params.put("title", title);
		params.put("pay_type", pay_type);
		params.put("invoice_need", invoice_need);
		params.put("urgency", urgency);
		params.put("startIndex", page.getStartIndex() - 1);
		params.put("currentItemCount", page.getCurrentItemCount());
		params.put("sort", sort);
		params.put("order", order);
		return paymentMapper.getAccountantPayList(params);
	}
	
	//获取财务待收发票列表
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Payment> getAccountantInvoiceList(List<Integer> ids, Integer id,
			Integer department_id, String title, String pay_type,
			String invoice_title, String sort, 
			String order, Page page){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("department_id", department_id);
		params.put("title", title);
		params.put("pay_type", pay_type);
		params.put("invoice_title", invoice_title);
		params.put("startIndex", page.getStartIndex() - 1);
		params.put("currentItemCount", page.getCurrentItemCount());
		params.put("sort", sort);
		params.put("order", order);
		return paymentMapper.getAccountantInvoiceList(params);
	}
	
	//获取出纳待出账列表
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Payment> getTellerPayList(List<Integer> ids, Integer id,
			Boolean urgency, Integer department_id, String title,
			String sort, String order, Page page){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("urgency", urgency);
		params.put("department_id", department_id);
		params.put("title", title);
		params.put("startIndex", page.getStartIndex() - 1);
		params.put("currentItemCount", page.getCurrentItemCount());
		params.put("sort", sort);
		params.put("order", order);
		return paymentMapper.getTellerPayList(params);
	}
	
	//获取所有出账申请
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<Payment> getAll(Integer id, Boolean urgency,
			Integer department_id, String title, String pay_type,
			String state, Boolean invoice_need, String invoice_state,
			String sort, String order, Page page){
		Map params = new HashMap();
		params.put("id", id);
		params.put("urgency", urgency);
		params.put("department_id", department_id);
		params.put("title", title);
		params.put("pay_type", pay_type);
		params.put("state", state);
		params.put("invoice_need", invoice_need);
		params.put("invoice_state", invoice_state);
		params.put("sort", sort);
		params.put("order", order);
		params.put("startIndex", page.getStartIndex() - 1);
		params.put("currentItemCount", page.getCurrentItemCount());
		return paymentMapper.getAll(params);
	}
	
	//按条件获取申请人出账申请数
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer getMyApplyCount(Integer applicant_id, Integer id,
			String title, Boolean urgency, String pay_type,
			Boolean invoice_need, String state, String invoice_state){
		Map params = new HashMap();
		params.put("applicant_id", applicant_id );
		params.put("id", id );
		params.put("title", title);
		params.put("urgency", urgency);
		params.put("pay_type", pay_type);
		params.put("invoice_need", invoice_need);
		params.put("state", state);
		params.put("invoice_state", invoice_state);
		return paymentMapper.getMyApplyCount(params);
	}
		
	//按条件获取总（区域）经理待审批出账申请数
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer getAuditCount(List<Integer> ids, Integer id,
			Integer department_id, String title, String pay_type,
			Boolean invoice_need, Boolean urgency){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("department_id", department_id);
		params.put("title", title);
		params.put("pay_type", pay_type);
		params.put("invoice_need", invoice_need);
		params.put("urgency", urgency);
		return paymentMapper.getAuditCount(params);
	}
		
	//按条件获取财务待出账申请数
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer getAccountantPayCount(List<Integer> ids, Integer id,
			Integer department_id, String title, String pay_type,
			Boolean invoice_need, Boolean urgency){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("department_id", department_id);
		params.put("title", title);
		params.put("pay_type", pay_type);
		params.put("invoice_need", invoice_need);
		params.put("urgency", urgency);
		return paymentMapper.getAccountantPayCount(params);
	}
		
	//按条件获取财务待收发票数
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer getAccountantInvoiceCount(List<Integer> ids, Integer id,
			Integer department_id, String title, String pay_type,
			String invoice_title){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("department_id", department_id);
		params.put("title", title);
		params.put("pay_type", pay_type);
		params.put("invoice_title", invoice_title);
		return paymentMapper.getAccountantInvoiceCount(params);
	}
	
	//按条件获取出纳待出账数
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer getTellerPayCount(List<Integer> ids, Integer id,
			Boolean urgency, Integer department_id, String title){
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("id", id);
		params.put("urgency", urgency);
		params.put("department_id", department_id);
		params.put("title", title);
		return paymentMapper.getTellerPayCount(params);
	}
	
	//按条件获取所有出账申请数
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Integer getAllCount(Integer id, Boolean urgency,
			Integer department_id, String title, String pay_type,
			String state, Boolean invoice_need, String invoice_state){
		Map params = new HashMap();
		params.put("id", id);
		params.put("urgency", urgency);
		params.put("department_id", department_id);
		params.put("title", title);
		params.put("pay_type", pay_type);
		params.put("state", state);
		params.put("invoice_need", invoice_need);
		params.put("invoice_state", invoice_state);
		return paymentMapper.getAllCount(params);
	}
	
	//按条件获取消息接收人
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Integer> getMessageReceiver(String role_name, List<Integer> ids){
		Map params = new HashMap();
		params.put("role_name", role_name);
		params.put("ids", ids);
		return paymentMapper.getMessageReceiver(params);
	}
	
	//按多个编号获取出账申请
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Payment> queryPaymentsByIds(List<Integer> ids){
		Map params = new HashMap();
		params.put("ids", ids);
		return paymentMapper.queryPaymentsByIds(params);
	}
	
}
