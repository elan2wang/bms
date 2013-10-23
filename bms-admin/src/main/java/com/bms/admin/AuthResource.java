/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.admin.pojo.Authority;
import com.bms.admin.pojo.RoleAuthority;
import com.bms.admin.service.AuthorityResourceService;
import com.bms.admin.service.AuthorityService;
import com.bms.admin.service.RoleAuthorityService;
import com.bms.common.ConfigUtil;
import com.bms.common.Page;
import com.bms.common.http.ErrorResponse;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;

/**
 * @author wangjian
 * @create 2013年8月22日 上午10:09:19
 * @update TODO
 * 
 * 
 */
@Path("/auths")
@Controller
public class AuthResource extends BaseResource {

	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private RoleAuthorityService roleAuthorityService;
	@Autowired
	private AuthorityResourceService authorityResourceService;
	
	@SuppressWarnings("unchecked") @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage,
			@QueryParam("mod_id") Integer mod_id, @QueryParam("auth_enable") Boolean auth_enable,
			@QueryParam("auth_type") String auth_type, @QueryParam("role_id") Integer role_id,
			@QueryParam("auth_name") String auth_name) {
		// 获取查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		Page page = new Page();
		params.put("page", page);
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);
		if (mod_id != null) params.put("mod_id", mod_id);
		if (auth_enable != null) params.put("auth_enable", auth_enable);
		if (auth_type != null) params.put("auth_type", auth_type);
		if (auth_name != null && !auth_name.equals("")) params.put("auth_name", auth_name);
		
		// 获取查询数据
		/* BEGIN 用于给角色分配权限时，默认选中已分配权限 */
		List<Integer> authList = new ArrayList<Integer>();
		if (role_id != null) {
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("role_id", role_id);
			List<RoleAuthority> myList = roleAuthorityService.queryRoleAuthorities(params2);
			if (myList != null) {
				for (RoleAuthority ra : myList) {
					authList.add(ra.getAuth_id());
				}
			}
		}
		/* END 用于给角色分配权限时，默认选中已分配权限 */
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<Authority> list = authorityService.queryAuthorities(params);
		for (Authority auth : list) {
			Map<String, Object> obj = auth.toMap();
			/* BEGIN 用于给角色分配权限时，默认选中已分配权限 */
			if (role_id != null) {
				if (authList.contains(auth.getAuth_id())) {
					obj.put("checked", true);
				} else {
					obj.put("checked", false);
				}
			}
			/* END 用于给角色分配权限时，默认选中已分配权限 */
			items.add(obj);
		}

		// 生成返回数据
		Map<String, Object> attrs = generateQueryResult(page, items);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}
	
	@SuppressWarnings("unchecked")
	@Path("/view") @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAuthority(@QueryParam("auth_id") Integer auth_id) {
		 Authority auth = authorityService.queryAuthorityById(auth_id);
		 if (auth == null) {
			 String request = uriInfo.getPath();
			 ErrorResponse response = new ErrorResponse(request, "20201", ConfigUtil.getValue("20201"));
			 return response.toJson();
		 }
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(auth.toMap());
		return response.toJson();
	}

	@Path("/add") @POST
	@Produces(MediaType.APPLICATION_JSON)
	public String addAuthority(@FormParam("auth_name") String auth_name, 
			@FormParam("auth_type") String auth_type, @FormParam("mod_id") Integer mod_id,
			@FormParam("auth_description") String auth_description) {
		Authority auth = new Authority();
		auth.setAuth_description(auth_description);
		auth.setAuth_enable(true);
		auth.setAuth_name(auth_name);
		auth.setAuth_type(auth_type);
		auth.setMod_id(mod_id);  
		
		authorityService.insertAuthority(auth);

		SuccessResponse response = new SuccessResponse(10000, "Add Authority Successfully");
		return response.toJson();
	}
	
	@Path("/update") @POST
	@Produces(MediaType.APPLICATION_JSON)
	public String updateAuthority(@FormParam("auth_name") String auth_name,
			@FormParam("auth_id") Integer auth_id, @FormParam("mod_id") Integer mod_id,
			@FormParam("auth_description") String auth_description,
			@FormParam("auth_type") String auth_type) {
		Authority auth = authorityService.queryAuthorityById(auth_id);
		auth.setAuth_description(auth_description);
		auth.setAuth_name(auth_name);
		auth.setAuth_type(auth_type);
		auth.setMod_id(mod_id);
		
		authorityService.updateAuthority(auth);

		SuccessResponse response = new SuccessResponse(10000, "Update Authority Successfully");
		return response.toJson();
	}
	
	@Path("/delete")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAuthority(@FormParam("auth_id") Integer auth_id) {
		Authority auth = authorityService.queryAuthorityById(auth_id);
		if (auth == null) {
			 String request = uriInfo.getPath();
			 ErrorResponse response = new ErrorResponse(request, "20201", ConfigUtil.getValue("20201"));
			 return response.toJson();
		}
		
		authorityService.deleteAuthority(auth_id);
		
		SuccessResponse response = new SuccessResponse(10000, "Delete Authority Successfully");
		return response.toJson();
	}
	
	@Path("/switch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String switch_enable(@FormParam("auth_id") Integer auth_id, @FormParam("enable") Boolean enable) {
		Authority auth = authorityService.queryAuthorityById(auth_id);
		if (auth == null) {
			 String request = uriInfo.getPath();
			 ErrorResponse response = new ErrorResponse(request, "20201", ConfigUtil.getValue("20201"));
			 return response.toJson();
		}
		auth.setAuth_enable(enable);
		authorityService.updateAuthority(auth);

		SuccessResponse response;
		if (enable) {
			response = new SuccessResponse(10000, "Enable Authority Successfully");
		} else {
			response = new SuccessResponse(10000, "Disable Authority Successfully");
		}
		return response.toJson();
	}
	
	@Path("/assign_res") @POST
	@Produces(MediaType.APPLICATION_JSON)
	public String assignResources(@FormParam("auth_id") Integer auth_id, @FormParam("resources") String resources) {
		Authority auth = authorityService.queryAuthorityById(auth_id);
		if (auth == null) {
			 String request = uriInfo.getPath();
			 ErrorResponse response = new ErrorResponse(request, "20201", ConfigUtil.getValue("20201"));
			 return response.toJson();
		}
		
		List<Integer> res_list = new ArrayList<Integer>();
		for (String res_id : resources.split(",")){
			if (res_id.equals("")) continue;
			res_list.add(Integer.parseInt(res_id));
		}
		
		authorityResourceService.assignResources(auth_id, res_list);

		SuccessResponse response = new SuccessResponse(10000, "Assign Resource To Authority Successfully");
		return response.toJson();
	}
}
