/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.admin.pojo.Account;
import com.bms.admin.pojo.AccountDepartment;
import com.bms.admin.pojo.Department;
import com.bms.admin.pojo.Profile;
import com.bms.admin.service.AccountDepartmentService;
import com.bms.admin.service.AccountService;
import com.bms.admin.service.DepartmentService;
import com.bms.admin.service.ProfileService;
import com.bms.common.ConfigUtil;
import com.bms.common.Page;
import com.bms.common.consts.MsgType;
import com.bms.common.consts.Role;
import com.bms.common.consts.State;
import com.bms.common.http.ErrorResponse;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.message.Publisher;
import com.bms.message.pojo.BmpMessage;
import com.bms.security.core.SecurityContextHolder;
import com.bms.finance.pojo.Payment;
import com.bms.finance.service.PaymentService;
import com.bms.finance.service.VaultService;

/**
 * @author AI
 * @create 2013年8月13日 下午1:22:14
 * @update TODO
 * 
 * 
 */
@Path("/payments")
@Controller
public class PaymentResource extends BaseResource {
	private static Logger log = LoggerFactory.getLogger(PaymentResource.class);
	
	@Context
	UriInfo uriInfo;
	
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountDepartmentService accountDepartmentService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private VaultService vaultService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getPayment(@QueryParam("id") Integer id){
		Payment payment = paymentService.queryPaymentById(id);
		if(payment == null){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20901");
			ErrorResponse response = new ErrorResponse(request, "20901", error_msg);
			return response.toJson();
		}
		Map attrs = generatePaymentMap(payment);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}
	
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addPayment(@FormParam("title") String title, @FormParam("money") Float money, 
			@FormParam("invoice_need") Boolean invoice_need, @FormParam("pay_type") String pay_type, 
			@FormParam("dst_card_num") String dst_card_num, @FormParam("dst_bank_name") String dst_bank_name, 
			@FormParam("dst_account_name") String dst_account_name, @FormParam("urgency") Boolean urgency, 
			@FormParam("applicant_comment") String applicant_comment){
		Integer applicant_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Integer department_id = accountService.queryAccountById(applicant_id).getDep_id();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(department_id);
		Payment payment = new  Payment(title, money, invoice_need, pay_type, dst_card_num,
				dst_bank_name, dst_account_name, urgency, applicant_id, department_id, applicant_comment);

		paymentService.insertPayment(payment);
		//发送消息
		List<Integer> receiver1 = paymentService.getMessageReceiver(Role.AREA_MANAGER, ids);
		String receivers = "";
		for(Integer receiver : receiver1){
			receivers += "," + receiver;
		}
		if(!receivers.equals("")){
			receivers = receivers.substring(1, receivers.length());
			BmpMessage message = new BmpMessage();
			message.setDst_list(receivers);
			String content = "您有一个新的待审批出账申请";
			message.setMsg_content(content);
			message.setMsg_type(MsgType.payment_msg.toString());
			message.setTrigger_time(payment.getApply_time());
			message.setTrigger_event(uriInfo.getRequestUri().toString());
			Publisher.publish(message);
		}
		//返回数据
		SuccessResponse response = new SuccessResponse(10000, "Add Payments Successfully");
		return response.toJson();

	}
	
	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updatePayment(@FormParam("id") Integer id, @FormParam("title") String title, @FormParam("money") Float money, 
			@FormParam("invoice_need") Boolean invoice_need, @FormParam("pay_type") String pay_type, 
			@FormParam("dst_card_num") String dst_card_num, @FormParam("dst_bank_name") String dst_bank_name, 
			@FormParam(" dst_account_name") String dst_account_name, @FormParam("urgency") Boolean urgency, 
			@FormParam("applicant_comment") String applicant_comment){
		Payment payment = paymentService.queryPaymentById(id);
		if(payment == null){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20901");
			ErrorResponse response = new ErrorResponse(request, "20901", error_msg);
			return response.toJson();
		}
		if(!payment.getState().equals("new") && !payment.getState().equals("disagreed")){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20902");
			ErrorResponse response = new ErrorResponse(request, "20902", error_msg);
			return response.toJson();
		}
		payment.setState(State.PAYMENT_NEW);
		payment.setTitle(title);
		payment.setMoney(money);
		payment.setInvoice_need(invoice_need);
		payment.setPay_type(pay_type);
		payment.setDst_card_num(dst_card_num);
		payment.setDst_bank_name(dst_bank_name);
		payment.setDst_account_name(dst_account_name);
		payment.setUrgency(urgency);
		payment.setApplicant_comment(applicant_comment);
		Integer last_modify_person = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		payment.setLast_modify_person(last_modify_person);
		Timestamp last_modify_time = new Timestamp(System.currentTimeMillis());
		payment.setLast_modify_time(last_modify_time);
		
		paymentService.updatePayment(payment);
		//发送消息
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(payment.getDepartment_id());
		List<Integer> receiver1 = paymentService.getMessageReceiver(Role.AREA_MANAGER, ids);
		List<Integer> receiver2 = paymentService.getMessageReceiver(Role.MANAGER, ids);
		String receivers = "";
		for(Integer receiver : receiver1){
			receivers += "," + receiver;
		}
		for(Integer receiver : receiver2){
			receivers += "," + receiver;
		}
		if(!receivers.equals("")){
			receivers = receivers.substring(1, receivers.length());
			BmpMessage message = new BmpMessage();
			message.setDst_list(receivers);
			String content = "编号为" + payment.getId() + "的出账申请已被修改，请您核对后再审批";
			message.setMsg_content(content);
			message.setMsg_type(MsgType.payment_msg.toString());
			message.setTrigger_time(last_modify_time);
			message.setTrigger_event(uriInfo.getRequestUri().toString());
			Publisher.publish(message);
		}
		//返回数据
		SuccessResponse response = new SuccessResponse(10000, "Update Payments Successfully");
		return response.toJson();
	}
	
	@Path("/destroy")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String destroyPayment(@FormParam("id") Integer id){
		Payment payment = paymentService.queryPaymentById(id);
		if(payment == null){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20901");
			ErrorResponse response = new ErrorResponse(request, "20901", error_msg);
			return response.toJson();
		}
		if(!payment.getState().equals(State.PAYMENT_NEW) && !payment.getState().equals(State.PAYMENT_AUDIT_DISAGREED)){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20902");
			ErrorResponse response = new ErrorResponse(request, "20902", error_msg);
			return response.toJson();
		}
		
		paymentService.deletePayment(id);
		//发送消息
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(payment.getDepartment_id());
		List<Integer> receiver1 = paymentService.getMessageReceiver(Role.AREA_MANAGER, ids);
		List<Integer> receiver2 = paymentService.getMessageReceiver(Role.MANAGER, ids);
		String receivers = "";
		for(Integer receiver : receiver1){
			receivers += "," + receiver;
		}
		for(Integer receiver : receiver2){
			receivers += "," + receiver;
		}
		if(!receivers.equals("")){
			receivers = receivers.substring(1, receivers.length());
			BmpMessage message = new BmpMessage();
			message.setDst_list(receivers);
			String content = "编号为" + payment.getId() + "的出账申请已被删除，请您核对后再审批";
			message.setMsg_content(content);
			message.setMsg_type(MsgType.payment_msg.toString());
			message.setTrigger_time(new Timestamp(System.currentTimeMillis()));
			message.setTrigger_event(uriInfo.getRequestUri().toString());
			Publisher.publish(message);
		}
		//返回数据
		SuccessResponse response = new SuccessResponse(10000, "Delete Payments Successfully");
		return response.toJson();
	}
	
	@Path("/audit")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String auditPayment(@FormParam("id") Integer id, @FormParam("audit_state") String audit_state,
			@FormParam("invoice_title") String invoice_title, @FormParam("manager_comment") String manager_comment){
		Payment payment = paymentService.queryPaymentById(id);
		if(payment == null){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20901");
			ErrorResponse response = new ErrorResponse(request, "20901", error_msg);
			return response.toJson();
		}
		Integer manager_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		if(audit_state.equals(State.PAYMENT_AUDIT_AGREED)){//如果总经理审批通过
			payment.setState(State.PAYMENT_AUDIT_AGREED);
			payment.setInvoice_title(invoice_title);
		}else{
			payment.setState(State.PAYMENT_AUDIT_DISAGREED);
		}
		payment.setManager_id(manager_id);
		payment.setManager_comment(manager_comment);
		Timestamp audit_time = new Timestamp(System.currentTimeMillis());
		payment.setAudit_time(audit_time);
		payment.setLast_modify_time(audit_time);
		Integer last_modify_person = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		payment.setLast_modify_person(last_modify_person);
		Timestamp last_modify_time = audit_time;
		payment.setLast_modify_time(last_modify_time);
		
		paymentService.auditPayment(payment);
		//发送消息
		Integer receiver1 = payment.getApplicant_id();
		BmpMessage message = new BmpMessage();
		message.setMsg_type(MsgType.payment_msg.toString());
		//发给申请人
		message.setDst_list(receiver1.toString());
		String content = "您的”" + payment.getTitle() + "“出账申请审批";
		if(payment.getState().equals("agreed")){
			content += "已通过";
		}else{
			content += "未通过";
		}
		message.setMsg_content(content);
		message.setTrigger_time(audit_time);
		message.setTrigger_event(uriInfo.getRequestUri().toString());
		Publisher.publish(message);
		if(payment.getState().equals(State.PAYMENT_AUDIT_AGREED)){
			//发给财务
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(payment.getDepartment_id());
			List<Integer> receiver2 = paymentService.getMessageReceiver(Role.ACCOUNTANT, ids);
			String receivers = "";
			for(Integer receiver : receiver2){
				receivers += "," + receiver;
			}
			if(!receivers.equals("")){
				receivers = receivers.substring(1, receivers.length());
				message.setDst_list(receivers);
				content = "您有一个新的待出账任务";
				message.setMsg_content(content);
				Publisher.publish(message);
			}
		}
		//返回数据
		SuccessResponse response = new SuccessResponse(10000, "Audit Payments Successfully");
		return response.toJson();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Path("/audit_batch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String batchAuditPayments(@FormParam("audit_ids") String idString){
		List<Integer> ids = new ArrayList<Integer>();
		for(String id : idString.split(",")){
			ids.add(Integer.parseInt(id));
		}
		String state = State.PAYMENT_AUDIT_AGREED;
		Integer manager_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Timestamp audit_time = new Timestamp(System.currentTimeMillis());
		paymentService.batchAuditPayments(ids, state, audit_time, manager_id);
		
		//发送消息
		List<Payment> payments = paymentService.queryPaymentsByIds(ids);
		List<Integer> departmentIds = new ArrayList<Integer>();
		BmpMessage message = new BmpMessage();
		message.setMsg_type(MsgType.payment_msg.toString());
		message.setTrigger_time(audit_time);
		//发给申请人
		for(Payment payment : payments){
			message.setDst_list(payment.getApplicant_id().toString());
			message.setMsg_content("您的“" + payment.getTitle() + "”出账申请审批已通过");
			message.setTrigger_event(uriInfo.getRequestUri().toString());
			Publisher.publish(message);
			departmentIds.add(payment.getDepartment_id());
		}
		//发给财务
		List<Integer> accountantIds = paymentService.getMessageReceiver(Role.ACCOUNTANT, departmentIds);
		String receivers = "";
		for(Integer receiver : accountantIds){
			receivers += "," + receiver;
		}
		if(!receivers.equals("")){
			receivers = receivers.substring(1, receivers.length());
			message.setDst_list(receivers);
			message.setMsg_content("您有一个新的待出账任务");
			Publisher.publish(message);
		}
		//返回数据
		SuccessResponse response = new SuccessResponse(10000, "Batch Audit Payments Successfully");
		return response.toJson();
		
	}
	
	@Path("/accountant_pay")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String accountantPay(@FormParam("id") Integer id,
			@FormParam("src_account") String src_account, @FormParam("invoice_title") String invoice_title,
			@FormParam("teller_id") Integer teller_id, @FormParam("accountant_comment") String accountant_comment){
		Payment payment = paymentService.queryPaymentById(id);
		if(payment == null){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20901");
			ErrorResponse response = new ErrorResponse(request, "20901", error_msg);
			return response.toJson();
		}
		payment.setSrc_account(src_account);
		payment.setInvoice_title(invoice_title);
		payment.setTeller_id(teller_id);
		payment.setAccountant_comment(accountant_comment);
		payment.setState(State.PAYMENT_ACCOUNTANT_PAYED);
		Timestamp accountant_pay_time = new Timestamp(System.currentTimeMillis());
		payment.setAccountant_pay_time(accountant_pay_time);
		Timestamp last_modify_time = accountant_pay_time;
		payment.setLast_modify_time(last_modify_time);
		Integer accountant_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		payment.setAccountant_id(accountant_id);
		Integer last_modify_person = accountant_id;
		payment.setLast_modify_person(last_modify_person);
		
		paymentService.accountantPay(payment);
		vaultService.addCounts(src_account, "payment");
		
		//发送消息
		BmpMessage message = new BmpMessage();
		message.setMsg_type(MsgType.payment_msg.toString());
		message.setDst_list(payment.getApplicant_id().toString());
		message.setTrigger_time(accountant_pay_time);
		message.setTrigger_event(uriInfo.getRequestUri().toString());
		String Content = "";
		if(payment.getPay_type().equals("transfer")){
			//发给申请人
			Content = "您的“" + payment.getTitle() + "”出账申请，财务已转账";
			message.setMsg_content(Content);
			Publisher.publish(message);
		}else{
			//发给申请人
			Content = "您的“" + payment.getTitle() + "”出账申请，财务已将现金交给出纳";
			message.setMsg_content(Content);
			Publisher.publish(message);
			//发给出纳
			Content = "您有一个新的待出账任务";
			message.setMsg_content(Content);
			message.setDst_list(teller_id.toString());
			Publisher.publish(message);
		}
		//返回数据
		SuccessResponse response = new SuccessResponse(10000, "Accountant Pay Successfully");
		return response.toJson();
		
	}
	
	@Path("/teller_pay")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String tellerPay(@FormParam("id") Integer id,
		@FormParam("teller_comment") String teller_comment){
		Payment payment = paymentService.queryPaymentById(id);
		if(payment == null){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20901");
			ErrorResponse response = new ErrorResponse(request, "20901", error_msg);
			return response.toJson();
		}
		payment.setState(State.PAYMENT_TELLER_PAYED);
		payment.setTeller_comment(teller_comment);
		Timestamp teller_pay_time = new Timestamp(System.currentTimeMillis());
		payment.setTeller_pay_time(teller_pay_time);
		Timestamp last_modify_time = teller_pay_time;
		payment.setLast_modify_time(last_modify_time);
		Integer last_modify_person = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		payment.setLast_modify_person(last_modify_person);
		
		paymentService.tellerPay(payment);
		//发送消息
		BmpMessage message = new BmpMessage();
		message.setMsg_type(MsgType.payment_msg.toString());
		message.setDst_list(payment.getApplicant_id().toString());
		String content = "您的“" + payment.getTitle() + "”出账申请，出纳已出账";
		message.setMsg_content(content);
		message.setTrigger_time(teller_pay_time);
		message.setTrigger_event(uriInfo.getRequestUri().toString());
		Publisher.publish(message);
		//返回数据
		SuccessResponse response = new SuccessResponse(10000, "Teller Pay Successfully");
		return response.toJson();
	}
	
	@Path("/confirm_invoice")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String confirmInvoice(@FormParam("id") Integer id){
		Payment payment = paymentService.queryPaymentById(id);
		if(payment == null){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20901");
			ErrorResponse response = new ErrorResponse(request, "20901", error_msg);
			return response.toJson();
		}
		Timestamp invoice_time = new Timestamp(System.currentTimeMillis());
		payment.setInvoice_time(invoice_time);
		payment.setInvoice_state(State.PAYMENT_INVOICE_CLOSED);
		payment.setState(State.PAYMENT_CLOSED);
		Timestamp last_modify_time = invoice_time;
		payment.setLast_modify_time(last_modify_time);
		Integer last_modify_person = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		payment.setLast_modify_person(last_modify_person);
		
		paymentService.confirmInvoice(payment);
		//发送消息
		BmpMessage message = new BmpMessage();
		message.setDst_list(payment.getApplicant_id().toString());
		String content = "您的“" + payment.getTitle() + "”出账申请，财务已收发票";
		message.setMsg_content(content);
		message.setMsg_type(MsgType.payment_msg.toString());
		message.setTrigger_time(invoice_time);
		message.setTrigger_event(uriInfo.getRequestUri().toString());
		Publisher.publish(message);
		//返回数据
		SuccessResponse response = new SuccessResponse(10000, "Confirm Invoice Successfully");
		return response.toJson();
	}
	
	//总经理要求财务重新出账
	@Path("/ask_accountant_to_repay")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String askAccountantToRepay(@FormParam("id") Integer id){
		Payment payment = paymentService.queryPaymentById(id);
		if(payment == null){
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20901");
			ErrorResponse response = new ErrorResponse(request, "20901", error_msg);
			return response.toJson();
		}
		Integer manager_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		payment.setManager_id(manager_id);
		Timestamp audit_time = new Timestamp(System.currentTimeMillis());
		payment.setState(State.PAYMENT_AUDIT_AGREED);
		payment.setAudit_time(audit_time);
		payment.setLast_modify_time(audit_time);
		Integer last_modify_person = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		payment.setLast_modify_person(last_modify_person);
		Timestamp last_modify_time = audit_time;
		payment.setLast_modify_time(last_modify_time);
		
		paymentService.auditPayment(payment);
		//发送消息
		BmpMessage message = new BmpMessage();
		message.setDst_list(payment.getAccountant_id().toString());
		String content = "编号为“" + payment.getId() + "”的出账申请，经理要求重新出账";
		message.setMsg_content(content);
		message.setMsg_type(MsgType.payment_msg.toString());
		message.setTrigger_time(audit_time);
		message.setTrigger_event(uriInfo.getRequestUri().toString());
		Publisher.publish(message);
		//返回数据
		SuccessResponse response = new SuccessResponse(10000, "Ask accountant to repay Successfully");
		return response.toJson();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/my_apply_list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMyApplyList(@QueryParam("id") Integer id, @QueryParam("title") String title,
			@QueryParam("urgency") Boolean urgency, @QueryParam("pay_type") String pay_type, 
			@QueryParam("invoice_need") Boolean invoice_need, @QueryParam("state") String state, 
			@QueryParam("invoice_state") String invoice_state, @QueryParam("sort") String sort,
			@QueryParam("order") String order, @QueryParam("start") Integer start, 
			@QueryParam("itemsPerPage") Integer itemsPerPage){
		Integer applicant_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Integer total = paymentService.getMyApplyCount(applicant_id, id,
				title, urgency, pay_type,
				invoice_need, state, invoice_state);
		
		Page page = setPage(start, itemsPerPage, total);
		
		List<Payment> myApplyList = paymentService.getMyApplyList(applicant_id, id, title, urgency, pay_type, 
				invoice_need, state, invoice_state, sort, order, page);
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(Payment payment : myApplyList){
			Map<String, Object> item = generatePaymentMap(payment);
			list.add(item);
		}
		Map<String, Object> map = generateQueryResult(page, list);
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(map);
		return response.toJson();
	}

	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/audit_list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAuditList(@QueryParam("id") Integer id, @QueryParam("title") String title,
			@QueryParam("urgency") Boolean urgency, @QueryParam("pay_type") String pay_type, 
			@QueryParam("invoice_need") Boolean invoice_need, @QueryParam("department_id") Integer department_id,  
			@QueryParam("sort") String sort, @QueryParam("order") String order, 
			@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage){
		Integer account_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		List<Integer> departmentIds = getHandleDepartmentIds(account_id);
		if(!departmentIds.isEmpty()){
			Integer total = paymentService.getAuditCount(departmentIds, id, 
					department_id, title, pay_type, invoice_need, urgency);
			
			Page page = setPage(start, itemsPerPage, total);
			
			List<Payment> auditList = paymentService.getAuditList(departmentIds, id, 
					department_id, title, pay_type, urgency, invoice_need, sort, order, page);
			
			List list = new ArrayList<LinkedHashMap<String, Object>>();
			for(Payment payment : auditList){
				Map<String, Object> item = generatePaymentMap(payment);
				list.add(item);
			}
			Map<String, Object> map = generateQueryResult(page, list);
			
			NormalResponse response = new NormalResponse();
			response.setAttrs(map);
			return response.toJson();
		}else{
			Integer total = 0;
			Page page = setPage(start, itemsPerPage, total);
			List list = new ArrayList<LinkedHashMap<String, Object>>();
			Map<String, Object> map = generateQueryResult(page, list);
			NormalResponse response = new NormalResponse();
			response.setAttrs(map);
			return response.toJson();
		}
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/accountant_pay_list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccountantPayList(@QueryParam("id") Integer id, @QueryParam("department_id") Integer department_id, 
			@QueryParam("title") String title, @QueryParam("pay_type") String pay_type,
			@QueryParam("invoice_need") Boolean invoice_need, @QueryParam("urgency") Boolean urgency, 
			@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage, 
			@QueryParam("sort") String sort, @QueryParam("order") String order){
		Integer account_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		List<Integer> departmentIds = getHandleDepartmentIds(account_id);
		if(!departmentIds.isEmpty()){
			Integer total = paymentService.getAccountantPayCount(departmentIds, id, 
					department_id, title, pay_type, invoice_need, urgency);
			Page page = setPage(start, itemsPerPage, total);
			List<Payment> accountantPayList = paymentService.getAccountantPayList(departmentIds, id, department_id, 
					title, pay_type, invoice_need, urgency, sort, order, page);
			
			List list = new ArrayList<LinkedHashMap<String, Object>>();
			for(Payment payment : accountantPayList){
				Map<String, Object> item = generatePaymentMap(payment);
				list.add(item);
			}
			Map<String, Object> map = generateQueryResult(page, list);
			
			NormalResponse response = new NormalResponse();
			response.setAttrs(map);
			return response.toJson();
		}else{
			Integer total = 0;
			Page page = setPage(start, itemsPerPage, total);
			List list = new ArrayList<LinkedHashMap<String, Object>>();
			Map<String, Object> map = generateQueryResult(page, list);
			NormalResponse response = new NormalResponse();
			response.setAttrs(map);
			return response.toJson();
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/accountant_invoice_list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccountantInvoiceList(@QueryParam("id") Integer id,
			@QueryParam("department_id") Integer department_id, @QueryParam("title") String title, 
			@QueryParam("pay_type") String pay_type, @QueryParam("invoice_title") String invoice_title, 
			@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage,
			@QueryParam("sort") String sort, @QueryParam("order") String order){
		Integer account_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		List<Integer> departmentIds = getHandleDepartmentIds(account_id);
		if(!departmentIds.isEmpty()){
			Integer total = paymentService.getAccountantInvoiceCount(departmentIds, id,
					department_id, title, pay_type,
					invoice_title);
			Page page = setPage(start, itemsPerPage, total);
			List<Payment> accountantInvoiceList = paymentService.getAccountantInvoiceList(departmentIds, id, 
					department_id, title, pay_type, invoice_title, sort, order, page);
			
			List list = new ArrayList<LinkedHashMap<String, Object>>();
			for(Payment payment : accountantInvoiceList){
				Map<String, Object> item = generatePaymentMap(payment);
				list.add(item);
			}
			Map<String, Object> map = generateQueryResult(page, list);
			
			NormalResponse response = new NormalResponse();
			response.setAttrs(map);
			return response.toJson();
		}else{
			Integer total = 0;
			Page page = setPage(start, itemsPerPage, total);
			List list = new ArrayList<LinkedHashMap<String, Object>>();
			Map<String, Object> map = generateQueryResult(page, list);
			NormalResponse response = new NormalResponse();
			response.setAttrs(map);
			return response.toJson();
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/teller_pay_list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getTellerPayList(@QueryParam("id") Integer id,
			@QueryParam("urgency") Boolean urgency, @QueryParam("department_id") Integer department_id, 
			@QueryParam("title") String title, @QueryParam("start") Integer start, 
			@QueryParam("itemsPerPage") Integer itemsPerPage, @QueryParam("sort") String sort, 
			@QueryParam("order") String order){
		Integer account_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		List<Integer> departmentIds = getHandleDepartmentIds(account_id);
		if(!departmentIds.isEmpty()){
			Integer total = paymentService.getTellerPayCount(departmentIds, id,
					urgency, department_id, title);
			Page page = setPage(start, itemsPerPage, total);
			List<Payment> tellerPayList = paymentService.getTellerPayList(departmentIds, id, 
					urgency, department_id, title, sort, order, page);
			
			List list = new ArrayList<LinkedHashMap<String, Object>>();
			for(Payment payment : tellerPayList){
				Map<String, Object> item = generatePaymentMap(payment);
				list.add(item);
			}
			
			Map<String, Object> map = generateQueryResult(page, list);
			
			NormalResponse response = new NormalResponse();
			response.setAttrs(map);
			return response.toJson();
		}else{
			Integer total = 0;
			Page page = setPage(start, itemsPerPage, total);
			List list = new ArrayList<LinkedHashMap<String, Object>>();
			Map<String, Object> map = generateQueryResult(page, list);
			NormalResponse response = new NormalResponse();
			response.setAttrs(map);
			return response.toJson();
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAll(@QueryParam("id") Integer id, 
			@QueryParam("urgency") Boolean urgency, @QueryParam("department_id") Integer department_id, 
			@QueryParam("title") String title, @QueryParam("pay_type") String pay_type, 
			@QueryParam("state") String state, @QueryParam("invoice_need") Boolean invoice_need, 
			@QueryParam("invoice_state") String invoice_state, @QueryParam("start") Integer start, 
			@QueryParam("itemsPerPage") Integer itemsPerPage, @QueryParam("sort") String sort, 
			@QueryParam("order") String order){
		Integer total = paymentService.getAllCount(id, urgency,
				department_id, title, pay_type,
				state, invoice_need, invoice_state);
		Page page = setPage(start, itemsPerPage, total);
		List<Payment> allPaymentsList = paymentService.getAll(id, urgency, department_id, 
				title, pay_type, state, invoice_need, invoice_state, sort, order, page);
		
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(Payment payment : allPaymentsList){
			Map<String, Object> item = generatePaymentMap(payment);
			list.add(item);
		}
		Map<String, Object> map = generateQueryResult(page, list);
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(map);
		return response.toJson();
	}
	
	/**
	 * 
	 * TODO
	 * 根据部门编号获取对应的出纳
	 * @param TODO
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/get_related_tellers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRelatedTellers(@QueryParam("id") Integer id){
		Payment payment = paymentService.queryPaymentById(id);
		Integer dep_id = payment.getDepartment_id();
		List<Account> accounts = accountService.queryAccountsByDepartmentId(dep_id);
		List<Profile> profiles = profileService.queryProfiles();
		
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(Account account : accounts){
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			for(Profile profile : profiles){
				if(account.getAccount_id() == profile.getAccount_id()){
					map.put("account_id", account.getAccount_id());
					map.put("name", profile.getRealname());
					break;
				}
			}
			list.add(map);
		}
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("items", list);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
		
	}
	
	/**
	 * 
	 * TODO
	 * 获取所能操作的部门（用于加载下拉框）
	 * @param TODO
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/get_handle_departments")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getDepartments(){
		Integer account_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		List<Integer> departmentIds = getHandleDepartmentIds(account_id);
		List<Department> departments = departmentService.queryDepartments(new LinkedHashMap());
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(Integer dep_id : departmentIds){
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			for(Department department : departments){
				if(department.getDep_id() == dep_id){
					map.put("dep_id", dep_id);
					map.put("dep_name", department.getDep_name());
					break;
				}
			}
			list.add(map);
		}
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("items", list);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}

	/**
	 * TODO
	 * 根据start itemsPerPage total获取Page
	 * @param TODO
	 * @return Page
	 */
	private Page setPage(Integer start, Integer itemsPerPage, Integer total) {
		Page page = new Page();
		if(start == null){
			start = page.getStartIndex();
		}else{
			page.setStartIndex(start);
		}
		if(itemsPerPage == null){
			itemsPerPage = page.getItemsPerPage();
		}else{
			page.setItemsPerPage(itemsPerPage);
		}
		Integer currentItemCount = 
				(total - start + 1) >= page.getItemsPerPage() ? page.getItemsPerPage() : (total - start + 1);
		page.setCurrentItemCount(currentItemCount);
		page.setTotalItems(total);
		return page;
	}
	
	/**
	 * 
	 * 根据payment生成Map
	 *
	 * @param TODO
	 * @return Map<String,Object>
	 */
	public Map<String, Object> generatePaymentMap(Payment payment){
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("id", payment.getId());
		attrs.put("title", payment.getTitle());
		attrs.put("money", payment.getMoney());
		attrs.put("invoice_need", payment.getInvoice_need());
		attrs.put("invoice_title", payment.getInvoice_title());
		attrs.put("pay_type", payment.getPay_type());
		attrs.put("src_account", payment.getSrc_account());
		attrs.put("dst_card_num", payment.getDst_card_num());
		attrs.put("dst_bank_name", payment.getDst_bank_name());
		attrs.put("dst_account_name", payment.getDst_account_name());
		attrs.put("urgency", payment.getUrgency());
		attrs.put("apply_time", payment.getApply_time());
		attrs.put("audit_time", payment.getAudit_time());
		attrs.put("accountant_pay_time", payment.getAccountant_pay_time());
		attrs.put("teller_pay_time", payment.getTeller_pay_time());
		attrs.put("invoice_time", payment.getInvoice_time());
		attrs.put("applicant", "");
		attrs.put("teller", "");
		attrs.put("manager", "");
		attrs.put("accountant", "");
		attrs.put("last_modify_person", "");
		if(payment.getApplicant_id() != null){
			attrs.put("applicant", 
					accountService.queryAccountById(payment.getApplicant_id()).getUsername());
		}
		if(payment.getTeller_id() != null){
			attrs.put("teller", 
					accountService.queryAccountById(payment.getTeller_id()).getUsername());
		}
		if(payment.getManager_id() != null){
			attrs.put("manager", 
					accountService.queryAccountById(payment.getManager_id()).getUsername());
		}
		if(payment.getAccountant_id()!= null){
			attrs.put("accountant", 
					accountService.queryAccountById(payment.getAccountant_id()).getUsername());
		}
		if(payment.getLast_modify_person() != null){
			attrs.put("last_modify_person", 
					accountService.queryAccountById(payment.getLast_modify_person()).getUsername());
		}
		attrs.put("department", 
				departmentService.queryDepartment(payment.getDepartment_id()).getDep_name());
		attrs.put("applicant_comment", payment.getApplicant_comment());
		attrs.put("teller_comment", payment.getTeller_comment());
		attrs.put("manager_comment", payment.getManager_comment());
		attrs.put("accoutant_comment", payment.getAccountant_comment());
		attrs.put("invoice_state", payment.getInvoice_state());
		attrs.put("state", payment.getState());
		attrs.put("last_modify_time", payment.getLast_modify_time());
		return attrs;
	}
	
	
	/**
	 * 获取当前用户可操作的部门编号
	 *
	 * @param TODO
	 * @return List<Integer>
	 */
	private List<Integer> getHandleDepartmentIds(Integer account_id) {
		List<AccountDepartment> accountDepartmentList = accountDepartmentService.queryAccountDepartments(account_id);
		List<Integer> departmentIds = new ArrayList<Integer>();
		for(AccountDepartment accountDepartment : accountDepartmentList){
			departmentIds.add(accountDepartment.getDep_id());
		}
		return departmentIds;
	}
}
