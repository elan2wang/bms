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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.admin.pojo.AccountRole;
import com.bms.admin.pojo.Module;
import com.bms.admin.pojo.Role;
import com.bms.admin.pojo.RoleModule;
import com.bms.admin.service.AccountRoleService;
import com.bms.admin.service.ModuleService;
import com.bms.admin.service.RoleAuthorityService;
import com.bms.admin.service.RoleModuleService;
import com.bms.admin.service.RoleService;
import com.bms.common.ConfigUtil;
import com.bms.common.Page;
import com.bms.common.http.ErrorResponse;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author wangjian
 * @create 2013年8月15日 下午12:21:30
 * @update TODO
 * 
 * 
 */
@Path("/roles")
@Controller
public class RoleResource extends BaseResource{
	
	private static Logger log = LoggerFactory.getLogger(RoleResource.class);
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleAuthorityService roleAuthorityService;
	@Autowired
	private RoleModuleService roleModuleService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private AccountRoleService accountRoleService;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam("start") Integer start,  @QueryParam("itemsPerPage") Integer itemsPerPage, 
			@QueryParam("role_level") Integer role_level, @QueryParam("role_enable") Boolean role_enable, 
			@QueryParam("role_type") Integer role_type, @QueryParam("role_name") String role_name,
			@QueryParam("account_id") Integer account_id) {
		// 获取参数
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		Page page = new Page();
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);
		params.put("page", page);
		if (role_level != null) params.put("role_level", role_level);
		if (role_enable != null) params.put("role_enable", role_enable);
		if (role_type != null) params.put("role_type", role_type);
		if (role_name != null && !role_name.equals("")) params.put("role_name", role_name);
		
		// 获取数据
		/* BEGIN 用于给用户分配角色时，默认选中已分配角色 */
		List<Integer> roleList = new ArrayList<Integer>();
		if (account_id != null) {
			List<AccountRole> myList = accountRoleService.queryAccountRoles(account_id);
			if (myList != null) {
				for (AccountRole ar : myList) {
					roleList.add(ar.getRole_id());
				}
			}
		}
		/* END 用于给用户分配角色时，默认选中已分配角色 */
		List<Role> list = roleService.queryRoles(params);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (Role role : list){
			Map<String, Object> item = role.toMap();
			/* BEGIN 用于给用户分配角色时，默认选中已分配角色 */
			if (account_id != null) {
				if (roleList.contains(role.getRole_id())) {
					item.put("checked", true);
				} else {
					item.put("checked", false);
				}
			}
			/* END 用于给用户分配角色时，默认选中已分配角色 */
			items.add(item);
		}
		// 生成返回数据
		Map<String, Object> attrs = generateQueryResult(page, items);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}
	
	@SuppressWarnings("unchecked")
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRole(@QueryParam("role_id") Integer role_id) {
		Role role = roleService.queryRole(role_id);
		if (role == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20101", ConfigUtil.getValue("20101"));
			return response.toJson();
		}
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(role.toMap());
		return response.toJson();
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addRole(@FormParam("role_name") String role_name,
			@FormParam("role_level") Integer role_level, @FormParam("role_type") String role_type,
			@FormParam("role_description") String role_description) {
		Role role = new Role();
		role.setRole_name(role_name);
		role.setRole_level(role_level);
		role.setRole_type(role_type);
		role.setRole_description(role_description);
		role.setRole_enable(true);
		roleService.insertRole(role);
		
		SuccessResponse response = new SuccessResponse(10000, "Add Role Successfully");
		return response.toJson();
	}
	
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateRole(@FormParam("role_id") Integer role_id, 
			@FormParam("role_name") String role_name, @FormParam("role_level") Integer role_level,
			@FormParam("role_type") String role_type, @FormParam("role_description") String role_description) {
		Role role = roleService.queryRole(role_id);
		if (role == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20101", ConfigUtil.getValue("20101"));
			return response.toJson();
		}
		role.setRole_name(role_name);
		role.setRole_level(role_level);
		role.setRole_type(role_type);
		role.setRole_description(role_description);
		log.debug(role_description);
		role.setRole_enable(true);
		roleService.updateRole(role);
		
		SuccessResponse response = new SuccessResponse(10000, "Update Role Successfully");
		return response.toJson();
	}
	
	@POST
	@Path("/switch")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String switchRole(@FormParam("role_id") Integer role_id, @FormParam("enable") Boolean enable) {
		Role role = roleService.queryRole(role_id);
		if (role == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20101", ConfigUtil.getValue("20101"));
			return response.toJson();
		}
		role.setRole_enable(enable);
		roleService.updateRole(role);
		
		SuccessResponse response;
		if (enable) {
			response = new SuccessResponse(10000, "Enable Role Successfully");
		} else {
			response = new SuccessResponse(10000, "Disable Role Successfully");
		}
		return response.toJson();
	}
	
	@POST
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateRole(@FormParam("role_id") Integer role_id) {
		Role role = roleService.queryRole(role_id);
		if (role == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20101", ConfigUtil.getValue("20101"));
			return response.toJson();
		}
		roleService.deleteRole(role_id);
		
		SuccessResponse response = new SuccessResponse(10000, "Delete Role Successfully");
		return response.toJson();
	}
	
	@POST
	@Path("/assign_auth")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String assignAuthorities(@FormParam("role_id") Integer role_id, 
			@FormParam("auth_ids") String auth_ids, @FormParam("mod_id") Integer mod_id) {
		if (role_id == null || role_id.equals("")) {
			String request = uriInfo.getRequestUri().toString();
			String error_msg = String.format(ConfigUtil.getValue("10010"), "role_id");
			ErrorResponse response = new ErrorResponse(request, "10010", error_msg);
			return response.toJson();
		}
		if (mod_id == null || mod_id.equals("")) {
			String request = uriInfo.getRequestUri().toString();
			String error_msg = String.format(ConfigUtil.getValue("10010"), "mod_id");
			ErrorResponse response = new ErrorResponse(request, "10010", error_msg);
			return response.toJson();
		}
		if (auth_ids == null) {
			String request = uriInfo.getRequestUri().toString();
			String error_msg = String.format(ConfigUtil.getValue("10010"), "auth_ids");
			ErrorResponse response = new ErrorResponse(request, "10010", error_msg);
			return response.toJson();
		}
		
		// 若auth_ids为空，表示取消了对该模块的授权
		if (auth_ids.equals("")) {
			roleAuthorityService.deleteAuthorities(role_id, mod_id);
			roleModuleService.deleteRoleModule(role_id, mod_id);
		} else {
			String[] ids = auth_ids.split(",");
			List<Integer> auths = new ArrayList<Integer>();
			for (String id : ids) {
				if (id.equals("")) continue;
				auths.add(Integer.parseInt(id));
			}
			roleAuthorityService.assignAuthorities(role_id, mod_id, auths);
			//判断是否需要授权模块
			RoleModule tmpRoleMod = roleModuleService.queryRoleModule(role_id, mod_id);
			if (tmpRoleMod == null) {
				roleModuleService.assignRoleModule(role_id, mod_id);
			}
			//判断是否需要授权该模块父模块
			Module mod = moduleService.queryModuleById(mod_id);
			RoleModule tmpRoleMod1 = roleModuleService.queryRoleModule(role_id, mod.getFather_mod());
			if (tmpRoleMod1 == null) {
				roleModuleService.assignRoleModule(role_id, mod.getFather_mod());
			}
		}
		
		SuccessResponse response = new SuccessResponse(10000, "Assign Authorities And Modules Successfully");
		return response.toJson();
	}
	
	
}
