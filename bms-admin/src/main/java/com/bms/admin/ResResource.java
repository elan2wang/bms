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

import javax.ws.rs.core.MediaType;

import com.bms.admin.pojo.AuthorityResource;
import com.bms.admin.pojo.Resource;
import com.bms.admin.service.AuthorityResourceService;
import com.bms.admin.service.ResourceService;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author wangjian
 * @create 2013年8月22日 下午2:01:06
 * @update TODO
 * 
 * 
 */
@Path("/resources")
@Controller
public class ResResource extends BaseResource {
	private static Logger log = LoggerFactory.getLogger(ResResource.class);

	@Autowired
	private ResourceService resourceService;
	@Autowired
	private AuthorityResourceService authorityResourceService;

	@SuppressWarnings("unchecked") @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam("mod_id") Integer mod_id, @QueryParam("res_enable") Boolean res_enable,
			@QueryParam("res_type") String res_type, @QueryParam("res_link") String res_link,
			@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage) {
		// 获取参数
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		Page page = new Page();
		params.put("page", page);
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);
		if (res_enable != null) params.put("res_enable", res_enable);
		if (mod_id != null && mod_id != 0) params.put("mod_id", mod_id);
		if (res_type != null && !res_type.equals("")) params.put("res_type", res_type);
		if (res_link != null && !res_link.equals("")) params.put("res_link", res_link);

		// 获取数据		
		List<Resource> list = resourceService.queryResources(params);
		List<Map<String, Object>> resObjList = new ArrayList<Map<String, Object>>();
		for (Resource res : list) resObjList.add(res.toMap());

		// 生成返回数据
		Map<String, Object> attrs = generateQueryResult(page, resObjList);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@GET @Path("/list_by_mod")
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam("mod_id") Integer mod_id, @QueryParam("auth_id") Integer auth_id,
			@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage,
			@QueryParam("res_enable") Boolean res_enable) {
		// 获取参数
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		Page page = new Page();
		params.put("page", page);
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);
		if (mod_id != null && mod_id != 0) params.put("mod_id", mod_id);
		if (res_enable != null) params.put("res_enable", res_enable);

		List<Integer> resList = new ArrayList<Integer>();
		if (auth_id != null) {
			List<AuthorityResource> myList = authorityResourceService.queryAuthorityResourcesByAuth(auth_id);
			if (myList != null) {
				for (AuthorityResource ar : myList) {
					resList.add(ar.getRes_id());
				}
			}
		}

		// 获取数据		
		List<Resource> list = resourceService.queryResources(params);
		List<Map<String, Object>> resObjList = new ArrayList<Map<String, Object>>();
		for (Resource res : list) {
			Map<String, Object> obj = res.toMap();
			if (resList.contains(res.getRes_id())) {
				obj.put("checked", true);
			} else {
				obj.put("checked", false);
			}
			resObjList.add(obj);
		}
		
		// 生成返回数据
		Map<String, Object> attrs = generateQueryResult(page, resObjList);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@GET @Path("/list_by_auth")
	@Produces(MediaType.APPLICATION_JSON)
	public String getResourcesByAuth(@QueryParam("auth_id") Integer auth_id,
			@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage) {
		if (auth_id == null || auth_id ==0) {
			String request = uriInfo.getAbsolutePath().toString();
			String error_msg = String.format(ConfigUtil.getValue("10010"), "auth_id");
			ErrorResponse response = new ErrorResponse(request, "10010", error_msg);
			return response.toJson();
		}

		Page page = new Page();
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);

		List<Resource> list = authorityResourceService.queryResourcesByAuth(auth_id, page);
		List<Map<String, Object>> resObjList = new ArrayList<Map<String, Object>>();
		for (Resource res : list) resObjList.add(res.toMap());

		// 生成返回数据
		Map<String, Object> attrs = generateQueryResult(page, resObjList);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}


	@SuppressWarnings("unchecked")
	@GET @Path("/view")
	@Produces(MediaType.APPLICATION_JSON)
	public String getResource(@QueryParam("res_id") Integer res_id) {
		Resource res = resourceService.queryResourceById(res_id);

		if (res == null) {
			String request = uriInfo.getAbsolutePath().toString();
			ErrorResponse response = new ErrorResponse(request, "20401", ConfigUtil.getValue("20401"));
			return response.toJson();
		}

		NormalResponse response = new NormalResponse();
		response.setAttrs(res.toMap());
		return response.toJson();
	}

	@POST @Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addResource(@FormParam("res_link") String res_link,
			@FormParam("res_type") String res_type, @FormParam("res_description") String res_description,
			@FormParam("mod_id") Integer mod_id) {
		Resource res = new Resource();
		res.setRes_description(res_description);
		res.setRes_enable(true);
		res.setRes_link(res_link);
		res.setRes_type(res_type);
		res.setMod_id(mod_id);

		resourceService.insertResource(res);

		SuccessResponse response = new SuccessResponse(10000, "Add Resource Successfully");
		return response.toJson();
	}

	@POST @Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateResource(@FormParam("res_id") Integer res_id, @FormParam("res_link") String res_link,
			@FormParam("res_type") String res_type, @FormParam("res_description") String res_description,
			@FormParam("mod_id") Integer mod_id) {
		Resource res = resourceService.queryResourceById(res_id);
		if (res == null) {
			String request = uriInfo.getAbsolutePath().toString();
			ErrorResponse response = new ErrorResponse(request, "20401", ConfigUtil.getValue("20401"));
			return response.toJson();
		}

		res.setRes_description(res_description);
		res.setRes_link(res_link);
		res.setRes_type(res_type);
		log.debug("mod_id    "+mod_id);
		res.setMod_id(mod_id);
		resourceService.updateResource(res);

		SuccessResponse response = new SuccessResponse(10000, "Update Resource Successfully");
		return response.toJson();
	}

	@Path("/delete")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteResource(@FormParam("res_id") Integer res_id) {
		Resource res = resourceService.queryResourceById(res_id);
		if (res == null) {
			String request = uriInfo.getAbsolutePath().toString();
			ErrorResponse response = new ErrorResponse(request, "20401", ConfigUtil.getValue("20401"));
			return response.toJson();
		}

		resourceService.deleteResource(res_id);

		SuccessResponse response = new SuccessResponse(10000, "Delete Resource Successfully");
		return response.toJson();
	}

	@Path("/switch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String switch_enable(@FormParam("res_id") Integer res_id, @FormParam("enable") Boolean enable) {
		Resource res = resourceService.queryResourceById(res_id);
		if (res == null) {
			String request = uriInfo.getAbsolutePath().toString();
			ErrorResponse response = new ErrorResponse(request, "20401", ConfigUtil.getValue("20401"));
			return response.toJson();
		}
		res.setRes_enable(enable);
		resourceService.updateResource(res);

		SuccessResponse response;
		if (enable) {
			response = new SuccessResponse(10000, "Enable Resource Successfully");
		} else {
			response = new SuccessResponse(10000, "Disable Resource Successfully");
		}
		return response.toJson();
	}
}
