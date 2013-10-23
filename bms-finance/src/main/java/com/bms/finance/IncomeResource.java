/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.FormParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.common.Page;
import com.bms.security.core.SecurityContextHolder;
import com.bms.admin.pojo.Department;
import com.bms.admin.service.AccountService;
import com.bms.admin.service.AccountDepartmentService;
import com.bms.admin.service.DepartmentService;
import com.bms.common.consts.MsgType;
import com.bms.common.consts.Role;
import com.bms.common.consts.State;
import com.bms.common.consts.Income_type;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.message.Publisher;
import com.bms.message.pojo.BmpMessage;
import com.bms.finance.pojo.Income;
import com.bms.finance.service.IncomeService;

/**
 * @author asus
 * @create 2013年8月15日 下午1:26:12
 * @update TODO
 * 
 * 
 */
@Path("/incomes")
@Controller
public class IncomeResource extends BaseResource{
	
	@Context
	UriInfo uriInfo;
	
	@Autowired
	private IncomeService incomeService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountDepartmentService accountDepartmentService;
	@Autowired
	private DepartmentService departmentService;
	
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getIncome(@QueryParam("id") Integer id) {
		Income income = incomeService.queryIncome(id);
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("id", income.getId());
		attrs.put("title", income.getTitle());
		attrs.put("money", income.getMoney());
		attrs.put("income_type", Income_type.myValueOf(income.getIncome_type()));
		attrs.put("bring_type", income.getBring_type());
		attrs.put("bank_card", income.getBank_card());
		attrs.put("serial_num", income.getSerial_num());
		attrs.put("invoice_type", income.getInvoice_type());
		attrs.put("invoice_title", income.getInvoice_title());
		attrs.put("generate_time", income.getGenerate_time());
		attrs.put("create_time", income.getCreate_time());
		attrs.put("audit_time", income.getAudit_time());
		attrs.put("invoice_time", income.getInvoice_time());
		attrs.put("handle_time", income.getHandle_time());
		attrs.put("creator", accountService.queryAccountById(income.getCreator_id()).getUsername());
		attrs.put("teller", "");
		attrs.put("accountant", "");
		attrs.put("manager", "");
		if(income.getTeller_id() != null){
			attrs.put("teller", accountService.queryAccountById(income.getTeller_id()).getUsername());
		}
		if(income.getAccountant_id() != null){
			attrs.put("accountant", accountService.queryAccountById(income.getAccountant_id()).getUsername());
		}
		if(income.getManager_id() != null){
			attrs.put("manager", accountService.queryAccountById(income.getManager_id()).getUsername());
		}
		attrs.put("department", departmentService.queryDepartment(income.getDepartment_id()).getDep_name());
		attrs.put("creator_comment", income.getCreator_comment());
		attrs.put("teller_comment", income.getTeller_comment());
		attrs.put("accountant_comment", income.getAccountant_comment());
		attrs.put("manager_comment", income.getManager_comment());
		attrs.put("invoice_state", income.getInvoice_state());
		attrs.put("state", income.getState());
		attrs.put("last_modify_time", income.getLast_modify_time());
		attrs.put("last_modify_person", accountService.queryAccountById(income.getLast_modify_person()).getUsername());
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		
		return response.toJson();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/my_apply_list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMyApplyList(@QueryParam("id") Integer id,@QueryParam("title") String title, 
			@QueryParam("income_type") Integer income_type, @QueryParam("bring_type") String bring_type,
			@QueryParam("invoice_type") String invoice_type, @QueryParam("invoice_state") String invoice_state,
			@QueryParam("state") String state, @QueryParam("order_type") String order_type,
			@QueryParam("start") Integer start, @QueryParam("items_per_page") Integer items_per_page){
		Integer creator_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Page page = new Page();
		if(start != null){
			page.setStartIndex(start);
		}
		if(items_per_page != null){
			page.setItemsPerPage(items_per_page);
		}
		page.setTotalItems(incomeService.countMyApplyList(creator_id, id, title, 
				income_type, bring_type, invoice_type, state, invoice_state));
		if(page.getTotalItems() - page.getStartIndex() < page.getItemsPerPage()){
			page.setCurrentItemCount(page.getTotalItems()-page.getStartIndex()+1);
		}else{
			page.setCurrentItemCount(page.getItemsPerPage());
		}
		
		page.setStartIndex(page.getStartIndex()-1);
		List<Income> income = incomeService.getMyApplyList(creator_id, id, title, income_type, bring_type, 
				invoice_type, state, invoice_state, order_type, page);
		page.setStartIndex(page.getStartIndex()+1);
		
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(int i = 0; i < income.size(); i++){
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", income.get(i).getId());
			map.put("title", income.get(i).getTitle());
			map.put("money", income.get(i).getMoney());
			map.put("income_type", Income_type.myValueOf(income.get(i).getIncome_type()));
			map.put("bring_type", income.get(i).getBring_type());
			map.put("invoice_type", income.get(i).getInvoice_type());
			map.put("state", income.get(i).getState());
			map.put("invoice_state", income.get(i).getInvoice_state());
			map.put("manager_comment", income.get(i).getManager_comment());
			map.put("creator_comment", income.get(i).getCreator_comment());
			map.put("create_time", income.get(i).getCreate_time());
			list.add(map);
		}
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(super.generateQueryResult(page, list));
		
		return response.toJson();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllIncomes(@QueryParam("income_type") Integer income_type, @QueryParam("bring_type") String bring_type,
			@QueryParam("invoice_type") String invoice_type, @QueryParam("department") Integer department,
			@QueryParam("invoice_state") String invoice_state,
			@QueryParam("state") String state, @QueryParam("order_type") String order_type,
			@QueryParam("start") Integer start, @QueryParam("items_per_page") Integer items_per_page){
		Integer creator_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		List<Integer> id_list = new ArrayList<Integer>();
		for(int i = 0;i < accountDepartmentService.queryAccountDepartments(creator_id).size();i++){
			id_list.add(accountDepartmentService.queryAccountDepartments(creator_id).get(i).getDep_id());
		}
		Page page = new Page();
		if(start != null){
			page.setStartIndex(start);
		}
		if(items_per_page != null){
			page.setItemsPerPage(items_per_page);
		}
		page.setTotalItems(incomeService.countAllIncomes(id_list, income_type, bring_type, invoice_type, 
				state, invoice_state, department));
		if(page.getTotalItems() - page.getStartIndex() < page.getItemsPerPage()){
			page.setCurrentItemCount(page.getTotalItems()-page.getStartIndex()+1);
		}else{
			page.setCurrentItemCount(page.getItemsPerPage());
		}
		
		page.setStartIndex(page.getStartIndex()-1);
		List<Income> income = incomeService.getAllIncomes(id_list, income_type, bring_type, 
				invoice_type, state, invoice_state, department, order_type, page);
		page.setStartIndex(page.getStartIndex()+1);
		
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(int i = 0; i < income.size(); i++){
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", income.get(i).getId());
			map.put("creator", accountService.queryAccountById(income.get(i).getCreator_id()).getUsername());
			map.put("department", departmentService.queryDepartment(income.get(i).getDepartment_id()).getDep_name());
			map.put("title", income.get(i).getTitle());
			map.put("money", income.get(i).getMoney());
			map.put("income_type", Income_type.myValueOf(income.get(i).getIncome_type()));
			map.put("bring_type", income.get(i).getBring_type());
			map.put("invoice_type", income.get(i).getInvoice_type());
			map.put("invoice_state", income.get(i).getInvoice_state());
			map.put("state", income.get(i).getState());
			map.put("create_time", income.get(i).getCreate_time());
			list.add(map);
		}
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(super.generateQueryResult(page, list));
		
		return response.toJson();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/account_checking_list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccountCheckingList(@QueryParam("id") Integer id, @QueryParam("bring_type") String bring_type,
			@QueryParam("invoice_type") String invoice_type, @QueryParam("department") Integer department,
			@QueryParam("invoice_state") String invoice_state, @QueryParam("order_type") String order_type,
			@QueryParam("start") Integer start, @QueryParam("items_per_page") Integer items_per_page){
		Integer creator_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		List<Integer> id_list = new ArrayList<Integer>();
		for(int i = 0;i < accountDepartmentService.queryAccountDepartments(creator_id).size();i++){
			id_list.add(accountDepartmentService.queryAccountDepartments(creator_id).get(i).getDep_id());
		}
		Page page = new Page();
		if(start != null){
			page.setStartIndex(start);
		}
		if(items_per_page != null){
			page.setItemsPerPage(items_per_page);
		}
		page.setTotalItems(incomeService.countAccountCheckingList(id_list, id, bring_type, 
				invoice_type, invoice_state, department));
		if(page.getTotalItems() - page.getStartIndex() < page.getItemsPerPage()){
			page.setCurrentItemCount(page.getTotalItems()-page.getStartIndex()+1);
		}else{
			page.setCurrentItemCount(page.getItemsPerPage());
		}
		
		page.setStartIndex(page.getStartIndex()-1);
		List<Income> income = incomeService.getAccountCheckingList(id_list, id, bring_type, 
				invoice_type, invoice_state, department, order_type, page);
		page.setStartIndex(page.getStartIndex()+1);
		
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(int i = 0; i < income.size(); i++){
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", income.get(i).getId());
			map.put("creator", accountService.queryAccountById(income.get(i).getCreator_id()).getUsername());
			map.put("department", departmentService.queryDepartment(income.get(i).getDepartment_id()).getDep_name());
			map.put("money", income.get(i).getMoney());
			map.put("bring_type", income.get(i).getBring_type());
			map.put("bank_card", income.get(i).getBank_card());
			map.put("serial_num", income.get(i).getSerial_num());
			map.put("invoice_type", income.get(i).getInvoice_type());
			map.put("invoice_state", income.get(i).getInvoice_state());
			map.put("create_time", income.get(i).getCreate_time());
			list.add(map);
		}
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(super.generateQueryResult(page, list));
		
		return response.toJson();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/invoice_list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getInvoiceList(@QueryParam("id") Integer id, @QueryParam("invoice_type") String invoice_type, 
			@QueryParam("department") Integer department, @QueryParam("state") String state,
			@QueryParam("order_type") String order_type,@QueryParam("start") Integer start, 
			@QueryParam("items_per_page") Integer items_per_page){
		Integer creator_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		List<Integer> id_list = new ArrayList<Integer>();
		for(int i = 0;i < accountDepartmentService.queryAccountDepartments(creator_id).size();i++){
			id_list.add(accountDepartmentService.queryAccountDepartments(creator_id).get(i).getDep_id());
		}
		Page page = new Page();
		if(start != null){
			page.setStartIndex(start);
		}
		if(items_per_page != null){
			page.setItemsPerPage(items_per_page);
		}
		page.setTotalItems(incomeService.countInvoiceList(id_list, id, invoice_type, state, department));
		if(page.getTotalItems() - page.getStartIndex() < page.getItemsPerPage()){
			page.setCurrentItemCount(page.getTotalItems()-page.getStartIndex()+1);
		}else{
			page.setCurrentItemCount(page.getItemsPerPage());
		}
		
		page.setStartIndex(page.getStartIndex()-1);
		List<Income> income = incomeService.getInvoiceList(id_list, id, invoice_type, state, department, order_type, page);
		page.setStartIndex(page.getStartIndex()+1);
		
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(int i = 0; i < income.size(); i++){
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", income.get(i).getId());
			map.put("creator", accountService.queryAccountById(income.get(i).getCreator_id()).getUsername());
			map.put("department", departmentService.queryDepartment(income.get(i).getDepartment_id()).getDep_name());
			map.put("money", income.get(i).getMoney());
			map.put("invoice_type", income.get(i).getInvoice_type());
			map.put("invoice_title", income.get(i).getInvoice_title());
			map.put("state", income.get(i).getState());
			map.put("create_time", income.get(i).getCreate_time());
			list.add(map);
		}
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(super.generateQueryResult(page, list));
		
		return response.toJson();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Path("/pending_list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getPendingList(@QueryParam("income_type") Integer income_type, @QueryParam("bring_type") String bring_type, 
			@QueryParam("invoice_type") String invoice_type, @QueryParam("department") Integer department, 
			@QueryParam("invoice_state") String invoice_state, @QueryParam("order_type") String order_type, 
			@QueryParam("start") Integer start, @QueryParam("items_per_page") Integer items_per_page){
		Page page = new Page();
		if(start != null){
			page.setStartIndex(start);
		}
		if(items_per_page != null){
			page.setItemsPerPage(items_per_page);
		}
		page.setTotalItems(incomeService.countPendingList(income_type, bring_type, invoice_type, invoice_state, department));
		if(page.getTotalItems() - page.getStartIndex() < page.getItemsPerPage()){
			page.setCurrentItemCount(page.getTotalItems()-page.getStartIndex()+1);
		}else{
			page.setCurrentItemCount(page.getItemsPerPage());
		}
		
		page.setStartIndex(page.getStartIndex()-1);
		List<Income> income = incomeService.getPendingList(income_type, bring_type, invoice_type, 
				invoice_state, department, order_type, page);
		page.setStartIndex(page.getStartIndex()+1);
		
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(int i = 0; i < income.size(); i++){
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", income.get(i).getId());
			map.put("creator", accountService.queryAccountById(income.get(i).getCreator_id()).getUsername());
			map.put("department", departmentService.queryDepartment(income.get(i).getDepartment_id()).getDep_name());
			map.put("money", income.get(i).getMoney());
			map.put("income_type", Income_type.myValueOf(income.get(i).getIncome_type()));
			map.put("bring_type", income.get(i).getBring_type());
			map.put("invoice_type", income.get(i).getInvoice_type());
			map.put("invoice_state", income.get(i).getInvoice_state());
			map.put("create_time", income.get(i).getCreate_time());
			list.add(map);
		}
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(super.generateQueryResult(page, list));
		
		return response.toJson();
	}
	
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addIncome(@FormParam("title") String title, @FormParam("money") Float money, 
			@FormParam("income_type") Integer income_type, @FormParam("bring_type") String bring_type,
			@FormParam("bank_card") String bank_card, @FormParam("serial_num") String serial_num,
			@FormParam("invoice_type") String invoice_type, @FormParam("invoice_title") String invoice_title,
			@FormParam("generate_time") Timestamp generate_time, 
			@FormParam("creator_comment") String creator_comment) {
		Integer creator_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Integer department_id = accountService.queryAccountById(creator_id).getDep_id();
		Income income = new Income(title, money, income_type, bring_type, bank_card, serial_num,
				invoice_type, invoice_title, generate_time, creator_id, department_id, creator_comment);
		
		incomeService.insertIncome(income);
		
		BmpMessage message = new BmpMessage();
		String receivers = "";
		if(invoice_type.equals("invoice_first")){
			Integer receivers_size = incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).size();
			for(int i = 0; i < receivers_size; i++){
				if(i == (receivers_size - 1)){
					receivers += incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).get(i);
				}else{
					receivers += incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).get(i) + ",";
				}
			}
		}else{
			Integer receivers_size = incomeService.creatorToOthers(Role.TELLER, department_id).size();
			for(int i = 0; i < receivers_size; i++){
				if(i == (receivers_size - 1)){
					receivers += incomeService.creatorToOthers(Role.TELLER, department_id).get(i);
				}else{
					receivers += incomeService.creatorToOthers(Role.TELLER, department_id).get(i) + ",";
				}
			}
		}
		message.setDst_list(receivers);
		message.setMsg_content("您有新的入账申请需要处理");
		message.setMsg_type(MsgType.income_msg.toString());
		message.setTrigger_time(new Timestamp(System.currentTimeMillis()));
		message.setTrigger_event("/bmp/1/incomes/add");
		Publisher.publish(message);
		
		SuccessResponse response = new SuccessResponse(10000, "Add Incomes Successfully");
		return response.toJson();
	}
	
	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateIncome(@FormParam("id") Integer id, @FormParam("title") String title, @FormParam("money") Float money, 
			@FormParam("income_type") Integer income_type, @FormParam("bring_type") String bring_type,
			@FormParam("bank_card") String bank_card, @FormParam("serial_num") String serial_num,
			@FormParam("invoice_type") String invoice_type, @FormParam("invoice_title") String invoice_title,
			@FormParam("generate_time") Timestamp generate_time, 
			@FormParam("creator_comment") String creator_comment) {
		Income income = incomeService.queryIncome(id);
		income.setTitle(title);
		income.setMoney(money);
		income.setIncome_type(income_type);
		income.setBring_type(bring_type);
		income.setBank_card(bank_card);
		income.setSerial_num(serial_num);
		income.setInvoice_type(invoice_type);
		income.setInvoice_title(invoice_title);
		income.setGenerate_time(generate_time);
		income.setCreator_comment(creator_comment);
		Integer last_modify_person = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		income.setLast_modify_person(last_modify_person);
		Timestamp last_modify_time = new Timestamp(System.currentTimeMillis());
		income.setLast_modify_time(last_modify_time);
		
		incomeService.updateIncome(income);
		
		BmpMessage message = new BmpMessage();
		String receivers = "";
		Integer department_id = income.getDepartment_id();
		if(invoice_type.equals("invoice_first")){
			Integer receivers_size = incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).size();
			for(int i = 0; i < receivers_size; i++){
				if(i == (receivers_size - 1)){
					receivers += incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).get(i);
				}else{
					receivers += incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).get(i) + ",";
				}
			}
		}else{
			Integer receivers_size = incomeService.creatorToOthers(Role.TELLER, department_id).size();
			for(int i = 0; i < receivers_size; i++){
				if(i == (receivers_size - 1)){
					receivers += incomeService.creatorToOthers(Role.TELLER, department_id).get(i);
				}else{
					receivers += incomeService.creatorToOthers(Role.TELLER, department_id).get(i) + ",";
				}
			}
		}
		message.setDst_list(receivers);
		message.setMsg_content("编号为" + income.getId() + "的入账申请被修改");
		message.setMsg_type(MsgType.income_msg.toString());
		message.setTrigger_time(new Timestamp(System.currentTimeMillis()));
		message.setTrigger_event("/bmp/1/incomes/update");
		Publisher.publish(message);
		
		SuccessResponse response = new SuccessResponse(10000, "Update Incomes Successfully");
		return response.toJson();
	}
	
	@Path("/destroy")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteIncome(@QueryParam("id") Integer id) {
		incomeService.deleteIncome(id);
		
		BmpMessage message = new BmpMessage();
		Income income = incomeService.queryIncome(id);
		String receivers = "";
		Integer department_id = income.getDepartment_id();
		if(income.getInvoice_type().equals("invoice_first")){
			Integer receivers_size = incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).size();
			for(int i = 0; i < receivers_size; i++){
				if(i == (receivers_size - 1)){
					receivers += incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).get(i);
				}else{
					receivers += incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).get(i) + ",";
				}
			}
		}else{
			Integer receivers_size = incomeService.creatorToOthers(Role.TELLER, department_id).size();
			for(int i = 0; i < receivers_size; i++){
				if(i == (receivers_size - 1)){
					receivers += incomeService.creatorToOthers(Role.TELLER, department_id).get(i);
				}else{
					receivers += incomeService.creatorToOthers(Role.TELLER, department_id).get(i) + ",";
				}
			}
		}
		message.setDst_list(receivers);
		message.setMsg_content("编号为" + income.getId() + "的入账申请被取消");
		message.setMsg_type(MsgType.income_msg.toString());
		message.setTrigger_time(new Timestamp(System.currentTimeMillis()));
		message.setTrigger_event("/bmp/1/incomes/destroy");
		Publisher.publish(message);
		
		SuccessResponse response = new SuccessResponse(10000, "Destroy Incomes Successfully");
		return response.toJson();
	}
	
	@Path("/account_checking")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String accountChecking(@FormParam("id") Integer id, @FormParam("equal") Boolean equal,
			@FormParam("teller_comment") String teller_comment) {
		Income income = incomeService.queryIncome(id);
		BmpMessage message = new BmpMessage();
		message.setMsg_type(MsgType.income_msg.toString());
		message.setTrigger_time(new Timestamp(System.currentTimeMillis()));
		message.setTrigger_event("/bmp/1/incomes/account_checking");
		if(equal){
			if(income.getInvoice_type().equals("invoice_last")){
				income.setState(State.INCOME_AUDITED);//若资金相符且为先款后票
				//给申请人发消息
				message.setDst_list(income.getCreator_id().toString());
				message.setMsg_content("主题为" + income.getTitle() + "的入账申请对账正常，已交付财务");
				Publisher.publish(message);
				//给财务发消息
				String receivers = "";
				Integer department_id = income.getDepartment_id();
				Integer receivers_size = incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).size();
				for(int i = 0; i < receivers_size; i++){
					if(i == (receivers_size - 1)){
						receivers += incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).get(i);
					}else{
						receivers += incomeService.creatorToOthers(Role.ACCOUNTANT, department_id).get(i) + ",";
					}
				}
				message.setDst_list(receivers);
				message.setMsg_content("编号为" + income.getId() + "的入账申请对账正常，请及时开票");
				Publisher.publish(message);
			}else{
				income.setState(State.INCOME_CLOSED);//若资金相符且为无票或先票
				//给申请人发消息
				message.setDst_list(income.getCreator_id().toString());
				message.setMsg_content("主题为" + income.getTitle() + "的入账申请对账正常，已结束");
				Publisher.publish(message);
			}
		}else{
			income.setState(State.INCOME_PENDING);//若资金不符则挂起
			//给申请人发消息
			message.setDst_list(income.getCreator_id().toString());
			message.setMsg_content("主题为" + income.getTitle() + "的入账申请对账异常，已交付总经理");
			Publisher.publish(message);
			//给总经理发消息
			String receivers = "";
			Integer department_id = income.getDepartment_id();
			Integer receivers_size = incomeService.creatorToOthers(Role.MANAGER, department_id).size();
			for(int i = 0; i < receivers_size; i++){
				if(i == (receivers_size - 1)){
					receivers += incomeService.creatorToOthers(Role.MANAGER, department_id).get(i);
				}else{
					receivers += incomeService.creatorToOthers(Role.MANAGER, department_id).get(i) + ",";
				}
			}
			message.setDst_list(receivers);
			message.setMsg_content("编号为" + income.getId() + "的入账申请对账异常，请及时处理");
			Publisher.publish(message);
		}
		Integer teller_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		income.setTeller_id(teller_id);
		income.setTeller_comment(teller_comment);
		Timestamp audit_time = new Timestamp(System.currentTimeMillis());
		income.setAudit_time(audit_time);
		income.setLast_modify_time(audit_time);
		income.setLast_modify_person(teller_id);
		
		incomeService.updateIncome(income);
		
		SuccessResponse response = new SuccessResponse(10000, "Check account Successfully");
		return response.toJson();
	}
	
	@Path("/handle_exception")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String handleException(@FormParam("id") Integer id, 
			@FormParam("manager_comment") String manager_comment) {
		Income income = incomeService.queryIncome(id);
		income.setState(State.INCOME_NEW);
		income.setInvoice_state(State.INCOME_INVOICE_OPEN);
		Integer manager_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		income.setManager_id(manager_id);
		income.setManager_comment(manager_comment);
		Timestamp handle_time = new Timestamp(System.currentTimeMillis());
		income.setHandle_time(handle_time);
		income.setLast_modify_time(handle_time);
		income.setLast_modify_person(manager_id);
		
		incomeService.updateIncome(income);
		
		BmpMessage message = new BmpMessage();
		message.setMsg_type(MsgType.income_msg.toString());
		message.setTrigger_time(new Timestamp(System.currentTimeMillis()));
		message.setTrigger_event("/bmp/1/incomes/handle_exception");
		//给申请人发消息
		message.setDst_list(income.getCreator_id().toString());
		message.setMsg_content("主题为" + income.getTitle() + "的入账申请已进行异常处理");
		Publisher.publish(message);
		//给出纳发消息
		message.setDst_list(income.getTeller_id().toString());
		message.setMsg_content("编号为" + income.getId() + "的入账申请已进行异常处理");
		Publisher.publish(message);
		//给财务发消息
		message.setDst_list(income.getAccountant_id().toString());
		Publisher.publish(message);
		
		SuccessResponse response = new SuccessResponse(10000, "Handle Exceptions Successfully");
		return response.toJson();
	}
	
	@Path("/invoice")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String invoice(@FormParam("id") Integer id, 
			@FormParam("accountant_comment") String accountant_comment) {
		Income income = incomeService.queryIncome(id);
		income.setInvoice_state(State.INCOME_INVOICE_CLOSED);
		if(income.getInvoice_type().equals("invoice_last")){
			income.setState(State.INCOME_CLOSED);//若为先款后票
		}
		Integer accountant_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		income.setAccountant_id(accountant_id);
		income.setAccountant_comment(accountant_comment);
		Timestamp invoice_time = new Timestamp(System.currentTimeMillis());
		income.setInvoice_time(invoice_time);
		income.setLast_modify_time(invoice_time);
		income.setLast_modify_person(accountant_id);
		
		incomeService.updateIncome(income);
		
		BmpMessage message = new BmpMessage();
		message.setMsg_type(MsgType.income_msg.toString());
		message.setTrigger_time(new Timestamp(System.currentTimeMillis()));
		message.setTrigger_event("/bmp/1/incomes/invoice");
		if(income.getInvoice_type().equals("invoice_last")){
			//给申请人发消息
			message.setDst_list(income.getCreator_id().toString());
			message.setMsg_content("主题为" + income.getTitle() + "的入账申请已开票，正常结束");
			Publisher.publish(message);
		}else if(income.getInvoice_type().equals("invoice_first")){
			//给申请人发消息
			message.setDst_list(income.getCreator_id().toString());
			message.setMsg_content("主题为" + income.getTitle() + "的入账申请已开票，已交付出纳");
			Publisher.publish(message);
			//给出纳发消息
			String receivers = "";
			Integer department_id = income.getDepartment_id();
			Integer receivers_size = incomeService.creatorToOthers(Role.TELLER, department_id).size();
			for(int i = 0; i < receivers_size; i++){
				if(i == (receivers_size - 1)){
					receivers += incomeService.creatorToOthers(Role.TELLER, department_id).get(i);
				}else{
					receivers += incomeService.creatorToOthers(Role.TELLER, department_id).get(i) + ",";
				}
			}
			message.setDst_list(receivers);
			message.setMsg_content("编号为" + income.getId() + "的入账申请已开票，请及时对账");
			Publisher.publish(message);
		}
		
		SuccessResponse response = new SuccessResponse(10000, "Invoice Successfully");
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
		Integer creator_id = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		List<Integer> id_list = new ArrayList<Integer>();
		for(int i = 0;i < accountDepartmentService.queryAccountDepartments(creator_id).size();i++){
			id_list.add(accountDepartmentService.queryAccountDepartments(creator_id).get(i).getDep_id());
		}
		List<Department> departments = departmentService.queryDepartments(new LinkedHashMap());
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(Integer dep_id : id_list){
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
}
