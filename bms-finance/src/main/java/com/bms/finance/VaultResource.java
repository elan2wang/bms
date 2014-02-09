/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.admin.pojo.Department;
import com.bms.admin.service.AccountService;
import com.bms.admin.service.DepartmentService;
import com.bms.admin.service.DepartmentVaultService;
import com.bms.common.Page;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.security.core.SecurityContextHolder;
import com.bms.finance.pojo.Vault;
import com.bms.finance.service.VaultService;

/**
 * @author AI
 * @create 2013-9-19 上午1:51:31
 * @update TODO
 * 
 * 
 */
@Path("/vaults")
@Controller
public class VaultResource extends BaseResource {

	private static Logger log = LoggerFactory.getLogger(VaultResource.class);
	
	@Autowired
	private VaultService vaultService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private DepartmentVaultService departmentVaultService;
	
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addVault(@FormParam("v_number") String v_number, @FormParam("type") String type,
			@FormParam("alias") String alias, @FormParam("comment") String comment, @FormParam("card_bank") String card_bank,
			@FormParam("card_owner") String card_owner){
		
		Integer last_modify_person = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		if(type.equals("vault")){
			v_number = "v" + System.currentTimeMillis();
		}
		Vault vault = new Vault(v_number, type, alias, comment, card_bank,
				card_owner, last_modify_person);
		/*Map map = new LinkedHashMap<String, Object>();
		map.put("v_number", v_number);
		map.put("type", type);
		map.put("alias", alias);
		map.put("comment", comment);
		map.put("card_bank", card_bank);
		map.put("card_owner", card_owner);
		map.put("vault_enable", vault.getVault_enable());
		NormalResponse response = new NormalResponse();
		response.setAttrs(vault.toMap());
		return response.toJson();*/
		
		
		vaultService.insertVault(vault);
		SuccessResponse response = new SuccessResponse(10000, "Add Vault Successfully");
		return response.toJson();
		
		
	}
	
	@Path("/destroy")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String destroyPayment(@FormParam("v_number") String v_number){
		vaultService.deleteVault(v_number);
		departmentVaultService.setDepartmentVaultHistory(v_number);
		SuccessResponse response = new SuccessResponse(10000, "Delete Vault Successfully");
		return response.toJson();
	}
	
	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateVault(@FormParam("v_number") String v_number, @FormParam("type") String type,
			@FormParam("alias") String alias, @FormParam("comment") String comment, @FormParam("card_bank") String card_bank,
			@FormParam("card_owner") String card_owner){
		Integer last_modify_person = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Vault vault = vaultService.queryVaultByV_number(v_number);
		vault.setAlias(alias);
		vault.setComment(comment);
		vault.setCard_bank(card_bank);
		vault.setCard_owner(card_owner);
		vault.setLast_modify_person(last_modify_person);
		vault.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		vaultService.updateVault(vault);
		SuccessResponse response = new SuccessResponse(10000, "Update Vault Successfully");
		return response.toJson();
	}
	
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getPayment(@QueryParam("v_number") String v_number){
		Vault vault = vaultService.queryVaultByV_number(v_number);
		NormalResponse response = new NormalResponse();
		response.setAttrs(vault.toMap());
		return response.toJson();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/all_vaults")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getVaultsList(@QueryParam("v_number") String v_number, @QueryParam("type") String type,
			@QueryParam("alias") String alias, @QueryParam("card_bank") String card_bank, 
			@QueryParam("card_owner") String card_owner, @QueryParam("sort") String sort,
			@QueryParam("order") String order, @QueryParam("start") Integer start, 
			@QueryParam("itemsPerPage") Integer itemsPerPage){
		Integer total = vaultService.getVaultCounts(v_number, type, alias, card_bank, card_owner);
		Page page = setPage(start, itemsPerPage, total);
		List<Vault> vaultList = vaultService.queryVaults(v_number, type, alias, card_bank, card_owner, sort, order, page);
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(Vault vault : vaultList){
			Map<String, Object> item = vault.toMap();
			list.add(item);
		}
		Map<String, Object> map = generateQueryResult(page, list);
		NormalResponse response = new NormalResponse();
		response.setAttrs(map);
		return response.toJson();
	}
	
	@Path("/get_current_available_vaults_by_dep_id")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAvailableVaultsByDep_id(@QueryParam("dep_id") Integer dep_id){
		List<Vault> availableVaults = vaultService.getAvailableVaults();
		List<Vault> currentVaults = vaultService.getCurrentVaultsByDep_id(dep_id);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();
		for(Vault vault : availableVaults){
			item = vault.toMap();
			item.put("checked", false);
			items.add(item);
		}
		for(Vault vault : currentVaults){
			item = vault.toMap();
			item.put("checked", true);
			items.add(item);
		}
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("items", items);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}

	
	//获取出入账申请时的账户
	/**
	 * 
	 * @Param "Income" OR "Payment"
	 */
	@Path("get_income_payment_vaults")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getIncomePaymentVaults(@QueryParam("type") String type){
		List<Vault> vaults = new ArrayList<Vault>();
		if(type.equals("income")){
			Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
			Integer dep_id = accountService.queryAccountById(uid).getDep_id();
			Department department = departmentService.queryDepartment(dep_id);
			if(department.getDep_level() == 3){
				vaults = vaultService.getCurrentVaultsByDep_id(dep_id);
			}else{
				Department dep = departmentService.queryDepartmentsByLevel(1).get(0);
				vaults = vaultService.getCurrentVaultsByDep_id(dep.getDep_id());
			}
		}if(type.equals("payment")){
			Department dep = departmentService.queryDepartmentsByLevel(1).get(0);
			vaults = vaultService.getCurrentVaultsByDep_id(dep.getDep_id());
		}
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for(Vault vault : vaults){
			items.add(vault.toMap());
		}
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("items", items);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}
	
	//设置账户的禁用启用
	@Path("/switch_vault_enable")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String switch_vault_enable(@FormParam("v_number") String v_number, 
			@FormParam("enable") Boolean vault_enable){
		vaultService.setVault_enable(v_number, vault_enable);
		String message = "";
		if(vault_enable){
			message = "Enable Vault Successfully";
		}else{
			message = "Disable Vault Successfully";
		}
		SuccessResponse response = new SuccessResponse(10000, message);
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
}
