/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.bms.admin.pojo.Account;
import com.bms.admin.pojo.Module;
import com.bms.admin.pojo.Role;
import com.bms.admin.service.AccountService;
import com.bms.admin.service.ModuleService;
import com.bms.admin.service.ProfileService;
import com.bms.admin.service.RoleService;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.security.SecuritySupport;
import com.bms.security.core.SecurityContextHolder;

/**
 * @author wangjian
 * @create 2013年8月4日 下午3:03:27
 * @update TODO
 * 
 * 
 */

@Path("/")
@Controller
public class AdminResource {
	private static Logger log = LoggerFactory.getLogger(AdminResource.class);
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private RoleService roleService;
	
	@Path("login") @POST
	@Produces(MediaType.APPLICATION_JSON)
	public String login() {
		log.info("Login successfully");
		SuccessResponse response = new SuccessResponse(10000, "Login Successfully");
		return response.toJson();
	}
	
	@Path("page_init") @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String page_init() {
		NormalResponse response = new NormalResponse();
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		// load user basic info
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		String username = SecurityContextHolder.getContext().getAuthenticationToken().getUsername();
		List<Integer> roles = SecurityContextHolder.getContext().getAuthenticationToken().getRoles();
		
		Account account = accountService.queryAccountById(uid);
		attrs.put("uid", uid);
		attrs.put("name", username);
		attrs.put("mobile", account.getMobile());
		// load role info
		List<Map<String, Object>> role_list = new ArrayList<Map<String, Object>>();
		for (Integer role_id : roles) {
			Role role = roleService.queryRole(role_id);
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("role_id", role.getRole_id());
			map.put("role_level", role.getRole_level());
			map.put("role_name", role.getRole_name());
			role_list.add(map);
		}
		attrs.put("roles", role_list);
		// load module info
		List<Module> modules = moduleService.queryMenuModules(1, roles);
		List<Map<String, Object>> mod_list = new ArrayList<Map<String, Object>>();
		for (Module mod : modules) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("mod_id", mod.getMod_id());
			map.put("mod_level", mod.getMod_level());
			map.put("mod_name", mod.getMod_name());
			map.put("mod_link", mod.getMod_link());
			map.put("mod_order", mod.getMod_order());
			mod_list.add(map);
		}
		attrs.put("modules", mod_list);
		
		response.setAttrs(attrs);
		return response.toJson();
	}
	
	@Path("tabs_init") @GET
	@SuppressWarnings("unchecked")
	@Produces(MediaType.APPLICATION_JSON)
	public String tabs_init(@QueryParam("father_mod") Integer father_mod) {
		List<Integer> roles = SecurityContextHolder.getContext().getAuthenticationToken().getRoles();
		List<Module> subModules = moduleService.querySubModules(father_mod, roles);
		
		NormalResponse response = new NormalResponse();
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> subs = new ArrayList<Map<String, Object>>();
		for (Module mod : subModules) {
			Map<String, Object> sub = mod.toMap();
			subs.add(sub);
		}
		attrs.put("tabs", subs);
		response.setAttrs(attrs);
		return response.toJson();
	}
	
	@Path("reload_security_meta_source") @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String reloadSecurityMetaSource() {
		SecuritySupport.reloadSecurityMetadataSource();
		
		SuccessResponse response = new SuccessResponse(10000, "reload security meta source successfully");
		return response.toJson();
	}
		
}
