/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin;

import java.security.NoSuchAlgorithmException;
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
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.admin.pojo.Account;
import com.bms.admin.service.AccountDepartmentService;
import com.bms.admin.service.AccountRoleService;
import com.bms.admin.service.AccountService;
import com.bms.admin.service.DepartmentService;
import com.bms.admin.service.RoleService;
import com.bms.common.ConfigUtil;
import com.bms.common.Crypto;
import com.bms.common.Page;
import com.bms.common.http.ErrorResponse;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.security.core.SecurityContextHolder;

/**
 * @author wangjian
 * @create 2013年8月12日 下午2:46:20
 * @update TODO
 * 
 * 
 */
@Path("/accounts")
@Controller
public class AccountResource extends BaseResource{
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountRoleService accountRoleService;
	@Autowired
	private AccountDepartmentService accountDepartmentService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private RoleService roleService;
	
	@GET
	@SuppressWarnings("unchecked")
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage,
			@QueryParam("account_enable") Boolean account_enable, @QueryParam("dep_id") Integer dep_id,
			@QueryParam("username") String username) {
		// 获取参数
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		Page page = new Page();
		params.put("page", page);
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);
		if (account_enable != null) params.put("account_enable", account_enable);
		if (dep_id != null) params.put("dep_id", dep_id);
		if (username != null && !username.equals("")) params.put("username", username);
		
		// 获取数据
		Map<Integer, String> depMap = getDepMap(departmentService);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<Account> list = accountService.queryAccounts(params);
		for (Account account : list){
			Map<String, Object> item = account.toMap();
			items.add(item);
			item.put("department", depMap.get(account.getDep_id()));
		}
		
		// 生成返回JSON
		Map<String, Object> attrs = generateQueryResult(page, items);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		
		return response.toJson();
	}
	
	@Path("/view") @GET
	@SuppressWarnings("unchecked")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccount(@QueryParam("account_id") Integer account_id) {
		Account account = accountService.queryAccountById(account_id);
		if (account == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20001", ConfigUtil.getValue("20001"));
			return response.toJson();
		}
		Map<String, Object> item = account.toMap();
		item.put("creator", accountService.queryAccountById(
				account.getAccount_id()).getUsername());
		item.put("last_modify_person", accountService.queryAccountById(
				account.getLast_modify_person()).getUsername());
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(item);
		
		return response.toJson();
	}
	
	@Path("/add") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addAccount(@FormParam("username") String username, 
			@FormParam("department") Integer department, @FormParam("password") String password, 
			@FormParam("email") String email, @FormParam("mobile") String mobile) throws NoSuchAlgorithmException {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		
		// 先用MD5加密，再用添加盐值的MD5加密
		String psd = Crypto.MD5Encrypt(password);
		
		Account account = new Account();
		account.setUsername(username);
		account.setPassword(psd);
		account.setEmail(email);
		account.setMobile(mobile);
		account.setDep_id(department);
		account.setCreator(uid);
		account.setLast_modify_person(uid);
		account.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		account.setAccount_enable(true);
		
		accountService.insertAccount(account);
		
		SuccessResponse response = new SuccessResponse(10000, "Add Account Successfully");
		return response.toJson();
	}
	
	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateAccount(@FormParam("account_id") Integer account_id, 
			@FormParam("username") String username, @FormParam("department") Integer department,
			@FormParam("email") String email, @FormParam("mobile") String mobile) {
		Account account = accountService.queryAccountById(account_id);
		account.setUsername(username);
		account.setEmail(email);
		account.setMobile(mobile);
		account.setDep_id(department);
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		account.setLast_modify_person(uid);
		account.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		
		accountService.updateAccount(account);
		
		SuccessResponse response = new SuccessResponse(10000, "Update Account Successfully");
		return response.toJson();
	}
	
	@Path("/reset") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String resetPassword(@FormParam("account_id") Integer account_id) {
		String origin_psd = "765760843A4A6D9BB2876FAD7F6FB164"; // 先用MD5加密，再用添加盐值的MD5加密
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		
		Account account = accountService.queryAccountById(account_id);
		account.setPassword(origin_psd);
		account.setLast_modify_person(uid);
		account.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		accountService.updateAccount(account);
		
		SuccessResponse response = new SuccessResponse(10000, "Reset Password Successfully");
		return response.toJson();
	}
	
	@Path("/password") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String changePassword(@FormParam("old_psd") String old_psd, @FormParam("new_psd") String new_psd) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Account account = accountService.queryAccountById(uid);
		if (!Crypto.MD5Encrypt(old_psd).equals(account.getPassword())) {
			String request = uriInfo.getRequestUri().toString(); 
			ErrorResponse response = new ErrorResponse(request, "20002", ConfigUtil.getValue("20002"));
			return response.toJson();
		}
		account.setPassword(Crypto.MD5Encrypt(new_psd));
		account.setLast_modify_person(uid);
		account.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		accountService.updateAccount(account);
		
		SuccessResponse response = new SuccessResponse(10000, "Change Password Successfully");
		return response.toJson();
	}
	
	@Path("/switch") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String switchAccount(@FormParam("account_id") Integer account_id, 
			@FormParam("enable") Boolean enable) {
		Account account = accountService.queryAccountById(account_id);
		account.setAccount_enable(enable);
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		account.setLast_modify_person(uid);
		account.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		accountService.updateAccount(account);
		String msg = null;
		if (enable) {
			msg = "Enable Account Successfully";
		} else {
			msg = "Disable Account Successfully";
		}
		SuccessResponse response = new SuccessResponse(10000, msg);
		return response.toJson();
	}
	
	@Path("/delete") @POST
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAccount(@FormParam("account_id") Integer account_id) {
		// if user not exist
		if (accountService.queryAccountById(account_id) == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20001", ConfigUtil.getValue("20001"));
			return response.toJson();
		}
		// if user exist
		accountService.deleteAccount(account_id);
		SuccessResponse response = new SuccessResponse(10000, "Delete Account Successfully");
		return response.toJson();
	}
	
	@Path("/assign_role") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String assignRole(@FormParam("account_id") Integer account_id,
			@FormParam("roles") String roles) {
		List<Integer> role_list = new ArrayList<Integer>();
		for (String role : roles.split(",")){
			role_list.add(Integer.parseInt(role));
		}
		accountRoleService.assignRole(account_id, role_list);
		
		Account account = accountService.queryAccountById(account_id);
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		account.setLast_modify_person(uid);
		account.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		accountService.updateAccount(account);
		
		SuccessResponse response = new SuccessResponse(10000, "Assign Roles Successfully");
		return response.toJson();
	}
	
	@Path("/assign_dep") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String assignDep(@FormParam("account_id") Integer account_id,
			@FormParam("deps") String deps) {
		List<Integer> dep_list = new ArrayList<Integer>();
		for (String dep : deps.split(",")){
			dep_list.add(Integer.parseInt(dep));
		}
		accountDepartmentService.assignDepartment(account_id, dep_list);
		
		Account account = accountService.queryAccountById(account_id);
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		account.setLast_modify_person(uid);
		account.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		accountService.updateAccount(account);
		
		SuccessResponse response = new SuccessResponse(10000, "Assign Departments Successfully");
		return response.toJson();
	}
	
}
