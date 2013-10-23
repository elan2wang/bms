/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.admin.pojo.Module;
import com.bms.admin.pojo.RoleModule;
import com.bms.admin.service.ModuleService;
import com.bms.admin.service.RoleModuleService;
import com.bms.common.ConfigUtil;
import com.bms.common.Page;
import com.bms.common.http.ErrorResponse;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;

/**
 * @author wangjian
 * @create 2013年7月23日 下午11:52:38
 * @update TODO
 * 
 * 
 */
@Path("/modules")
@Controller
public class ModuleResource extends BaseResource{
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private RoleModuleService roleModuleService;

	@SuppressWarnings("unchecked") @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage,
			@QueryParam("mod_level") Integer mod_level, @QueryParam("father_mod") Integer father_mod,
			@QueryParam("mod_enable") Integer mod_enable, @QueryParam("mod_type") Integer mod_type) {
		// 获取参数
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		Page page = new Page();
		params.put("page", page);
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);
		if (mod_level != null) params.put("mod_level", mod_level);
		if (father_mod != null) params.put("father_mod", father_mod);
		if (mod_enable != null) params.put("mod_enable", mod_enable);
		if (mod_type != null) params.put("mod_type", mod_type);

		// 获取数据
		List<Module> modules = moduleService.queryModules(params);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (Module mod : modules) {
			items.add(mod.toMap());
		}

		// 生成返回数据
		Map<String, Object> attrs = generateQueryResult(page, items);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@Path("/tree") @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String moduleTree(@QueryParam("role_id") Integer role_id) {
		List<Module> modules = moduleService.queryModules(new HashMap<String, Object>());

		/* BEGIN 用于给角色分配权限时，默认选中已分配权限 */
		List<Integer> modList = null;
		if (role_id != null) {
			List<RoleModule> myList = roleModuleService.queryRoleModules(role_id);
			modList = new ArrayList<Integer>();
			if (myList != null) {
				for (RoleModule rm : myList) {
					modList.add(rm.getMod_id());
				}
			}
		}
		/* END 用于给角色分配权限时，默认选中已分配权限 */

		//按层次获得所有模块
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> level_one_modules = new ArrayList<Map<String, Object>>();
		attrs.put("items", level_one_modules);
		Map<Integer, List<Map<String, Object>>> map = new LinkedHashMap<Integer, List<Map<String, Object>>>();
		for (Module mod : modules) {
			Map<String, Object> modObj = mod.toMap();
			/* BEGIN 用于给角色分配权限时，默认选中已分配权限 */
			if(role_id != null) {
				if (modList.contains(mod.getMod_id())) {
					modObj.put("checked", true);
				} else {
					modObj.put("checked", false);
				}
			}
			/* END 用于给角色分配权限时，默认选中已分配权限 */
			if (mod.getMod_level() == 1) {
				level_one_modules.add(modObj);
				if (!map.containsKey(mod.getMod_id())) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					map.put(mod.getMod_id(), list);
				}
			} else if (mod.getMod_level() == 2) {
				if (!map.containsKey(mod.getFather_mod())) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					list.add(modObj);
					map.put(mod.getFather_mod(), list);
				} else {
					map.get(mod.getFather_mod()).add(modObj);
				}
			}
		}
		for (Map<String, Object> level_one_module : level_one_modules) {
			Integer mod_id = (Integer) level_one_module.get("mod_id");
			level_one_module.put("subs", map.get(mod_id));
		}

		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}

	@Path("/add") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addModule(@FormParam("mod_name") String name,
			@FormParam("mod_link") String link, @FormParam("mod_type") String type,
			@FormParam("father_mod") Integer father_mod, @FormParam("mod_level") Integer level,
			@FormParam("mod_description") String description, @FormParam("mod_order") Double order) {

		Module module = new Module();
		module.setMod_name(name);
		module.setMod_link(link);
		module.setMod_type(type);
		module.setMod_description(description);
		module.setMod_order(order);
		module.setFather_mod(father_mod);
		module.setMod_level(level);
		module.setMod_enable(true);

		moduleService.insertModule(module);

		SuccessResponse response = new SuccessResponse(10000, "Add Module Successfully");
		return response.toJson();
	}

	@Path("/update") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String editModule(@FormParam("mod_id") Integer id, @FormParam("mod_name") String name,
			@FormParam("mod_link") String link, @FormParam("mod_type") String type,
			@FormParam("father_mod") Integer father_mod, @FormParam("mod_level") Integer level,
			@FormParam("mod_description") String description, @FormParam("mod_order") Double order) {

		Module module = moduleService.queryModuleById(id);
		if (module == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20301", ConfigUtil.getValue("20301"));
			return response.toJson();
		}

		module.setMod_name(name);
		module.setMod_link(link);
		module.setMod_type(type);
		module.setMod_description(description);
		module.setMod_order(order);
		module.setFather_mod(father_mod);
		module.setMod_level(level);

		moduleService.updateModule(module);

		SuccessResponse response = new SuccessResponse(10000, "Update Module Successfully");
		return response.toJson();
	}

	@Path("/switch") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String switchModule(@FormParam("mod_id") Integer mod_id, @FormParam("mod_enable") Boolean mod_enable) {
		Module module = moduleService.queryModuleById(mod_id);
		if (module == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20301", ConfigUtil.getValue("20301"));
			return response.toJson();
		}

		String msg = null;
		if (mod_enable) {
			module.setMod_enable(false);
			msg = "Disable Module Successfully";
		} else {
			module.setMod_enable(true);
			msg = "Enable Module Successfully";
		}
		moduleService.updateModule(module);

		SuccessResponse response = new SuccessResponse(10000, msg);
		return response.toJson();
	}

	@Path("/delete") @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteModule(@FormParam("mod_id") Integer mod_id) {
		Module module = moduleService.queryModuleById(mod_id);
		if (module == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20301", ConfigUtil.getValue("20301"));
			return response.toJson();
		}

		moduleService.deleteModule(mod_id);

		SuccessResponse response = new SuccessResponse(10000, "Delete Module Successfully");
		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@Path("/view") @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getModule(@QueryParam("mod_id") Integer mod_id) {
		Module module = moduleService.queryModuleById(mod_id);

		if (module == null) {
			String request = uriInfo.getPath();
			ErrorResponse response = new ErrorResponse(request, "20301", ConfigUtil.getValue("20301"));
			return response.toJson();
		}

		NormalResponse response = new NormalResponse();
		response.setAttrs(module.toMap());
		return response.toJson();
	}
}
