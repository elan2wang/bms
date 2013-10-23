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

import com.bms.admin.pojo.AccountDepartment;
import com.bms.admin.pojo.Department;
import com.bms.admin.service.AccountDepartmentService;
import com.bms.admin.service.DepartmentService;
import com.bms.admin.service.DepartmentVaultService;
import com.bms.common.Page;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;

/**
 * @author wangjian
 * @create 2013年8月21日 上午10:30:38
 * @update TODO
 * 
 * 
 */
@Path("departments")
@Controller
public class DepartmentResource extends BaseResource{

	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private AccountDepartmentService accountDepartmentService;
	@Autowired 
	private DepartmentVaultService departmentVaultService;
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam("start") Integer start,  @QueryParam("itemsPerPage") Integer itemsPerPage, 
			@QueryParam("account_id") Integer account_id) {
		// 获取参数
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		Page page = new Page();
		params.put("page", page);
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);
		
		// 获取数据
		/* BEGIN 用于给用户分配部门时，默认选中已分配部门 */
		List<Integer> depList = new ArrayList<Integer>();
		if (account_id != null) {
			List<AccountDepartment> myList = accountDepartmentService.queryAccountDepartments(account_id);
			if (myList != null) {
				for (AccountDepartment ad : myList) {
					depList.add(ad.getDep_id());
				}
			}
		}
		/* END 用于给用户分配部门时，默认选中已分配部门 */
		List<Department> list = departmentService.queryDepartments(params);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (Department dep : list){
			Map<String, Object> item = dep.toMap();
			/* BEGIN 用于给用户分配部门时，默认选中已分配部门 */
			if (account_id != null) {
				if (depList.contains(dep.getDep_id())) {
					item.put("checked", true);
				} else {
					item.put("checked", false);
				}
			}
			/* END 用于给用户分配部门时，默认选中已分配部门 */
			items.add(item);
		}
		
		Map<String, Object> attrs = generateQueryResult(page, items);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}
	
	@SuppressWarnings("unchecked")
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getDepartment(@QueryParam("dep_id") Integer dep_id){
		Department department = departmentService.queryDepartment(dep_id);
		NormalResponse response = new NormalResponse();
		response.setAttrs(department.toMap());
		return response.toJson();
	}
	
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addDepartment(@FormParam("dep_name") String dep_name,
			@FormParam("dep_level") Integer dep_level, @FormParam("address") String address){
		Department department = new Department(dep_level, dep_name, address);
		departmentService.addDepartment(department);
		SuccessResponse response = new SuccessResponse(10000, "Add Department Successfully");
		return response.toJson();
	}
	
	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addDepartment(@FormParam("dep_id") Integer dep_id, 
			@FormParam("dep_name") String dep_name, @FormParam("dep_level") Integer dep_level, 
			@FormParam("address") String address){
		Department department = departmentService.queryDepartment(dep_id);
		department.setDep_level(dep_level);
		department.setDep_name(dep_name);
		department.setAddress(address);
		departmentService.updateDepartment(department);
		SuccessResponse response = new SuccessResponse(10000, "Update Department Successfully");
		return response.toJson();
	}
	
	@Path("/destroy")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteDepartment(@FormParam("dep_id") Integer dep_id){
		departmentService.deleteDepartment(dep_id);
		SuccessResponse response = new SuccessResponse(10000, "Delete Department Successfully");
		return response.toJson();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/all_department")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getDepartmentsList(@QueryParam("dep_level") Integer dep_level, 
			@QueryParam("dep_name") String dep_name, @QueryParam("start") Integer start, 
			@QueryParam("itemsPerPage") Integer itemsPerPage){
		Integer total = departmentService.getDepartmentsCountByCondition(dep_level, dep_name);
		/*
		Map attrs = new HashMap();
		attrs.put("total", total);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
		*/
		Page page = setPage(start, itemsPerPage, total);
		List<Department> departmentList = departmentService.queryDepartmentsByCondition(dep_level, dep_name, page);
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(Department department : departmentList){
			Map<String, Object> item = department.toMap();
			list.add(item);
		}
		Map<String, Object> map = generateQueryResult(page, list);
		NormalResponse response = new NormalResponse();
		response.setAttrs(map);
		return response.toJson();
		
	}
	
	@Path("/assign_vaults")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String assignVaults(@FormParam("dep_id") Integer dep_id,
			@FormParam("vaults") String vaults){
		List<String> vault_list = new ArrayList<String>();
		for(String vault : vaults.split(",")){//如果前端没有选中任何选项，则split的结果为""，长度为1
			vault_list.add(vault);
		}
		departmentVaultService.assignVaults(dep_id, vault_list);
		SuccessResponse response = new SuccessResponse(10000, "Assign Vaults Successfully");
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
