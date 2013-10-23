/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.FormParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.common.Page;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.finance.pojo.InvoiceTitle;
import com.bms.finance.service.InvoiceTitleService;

/**
 * @author asus
 * @create 2013年9月22日 下午1:50:26
 * @update TODO
 * 
 * 
 */
@Path("/invoice_title")
@Controller
public class InvoiceTitleResource extends BaseResource {
	
	@Autowired
	private InvoiceTitleService invoiceTitleService;

	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addInvoiceTitle(@FormParam("title") String title){
		InvoiceTitle invoiceTitle = new InvoiceTitle();
		invoiceTitle.setTitle(title);
		invoiceTitle.setEnable(true);
		invoiceTitleService.insertInvoiceTitle(invoiceTitle);
		
		SuccessResponse response = new SuccessResponse(10000, "Add InvoiceTitle Successfully");
		return response.toJson();
	}
	
	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateInvoiceTitle(@FormParam("id") Integer id, @FormParam("title") String title){
		InvoiceTitle invoiceTitle = invoiceTitleService.queryInvoiceTitle(id);
		invoiceTitle.setTitle(title);
		invoiceTitleService.updateInvoiceTitle(invoiceTitle);
		
		SuccessResponse response = new SuccessResponse(10000, "Update InvoiceTitle Successfully");
		return response.toJson();
	}
	
	@POST
	@Path("/destroy")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteInvoiceTitle(@FormParam("id") Integer id){
		invoiceTitleService.deleteInvoiceTitle(id);
		
		SuccessResponse response = new SuccessResponse(10000, "Destroy InvoiceTitle Successfully");
		return response.toJson();
	}
	
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getInvoiceTitle(@QueryParam("id") Integer id) {
		InvoiceTitle invoiceTitle = invoiceTitleService.queryInvoiceTitle(id);
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("id", invoiceTitle.getId());
		attrs.put("title", invoiceTitle.getTitle());
		attrs.put("enable", invoiceTitle.getEnable());
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		
		return response.toJson();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getList(@QueryParam("title") String title, @QueryParam("enable") Boolean enable,
			@QueryParam("start") Integer start, @QueryParam("items_per_page") Integer items_per_page){
		Page page = new Page();
		if(start != null){
			page.setStartIndex(start);
		}
		if(items_per_page != null){
			page.setItemsPerPage(items_per_page);
		}
		page.setTotalItems(invoiceTitleService.countList(title, enable));
		if(page.getTotalItems() - page.getStartIndex() < page.getItemsPerPage()){
			page.setCurrentItemCount(page.getTotalItems()-page.getStartIndex()+1);
		}else{
			page.setCurrentItemCount(page.getItemsPerPage());
		}
		
		page.setStartIndex(page.getStartIndex()-1);
		List<InvoiceTitle> invoiceTitle = invoiceTitleService.getList(title, enable, page);
		page.setStartIndex(page.getStartIndex()+1);
		
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(int i = 0;i < invoiceTitle.size(); i++){
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", invoiceTitle.get(i).getId());
			map.put("title", invoiceTitle.get(i).getTitle());
			map.put("enable", invoiceTitle.get(i).getEnable());
			list.add(map);
		}
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(super.generateQueryResult(page, list));
		return response.toJson();
	}
	
	@POST
	@Path("/switch")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String switchEnable(@FormParam("id") Integer id, @FormParam("enable") Boolean enable) {
		InvoiceTitle invoiceTitle = invoiceTitleService.queryInvoiceTitle(id);
		invoiceTitle.setEnable(enable);
		invoiceTitleService.updateInvoiceTitle(invoiceTitle);
		
		SuccessResponse response;
		if (enable) {
			response = new SuccessResponse(10000, "Enable InvoiceTitle Successfully");
		} else {
			response = new SuccessResponse(10000, "Disable InvoiceTitle Successfully");
		}
		return response.toJson();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/get_titles")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getTitles(){
		List<InvoiceTitle> titles = invoiceTitleService.queryInvoiceTitles();
		List list = new ArrayList<LinkedHashMap<String, Object>>();
		for(InvoiceTitle invoiceTitle : titles){
			if(invoiceTitle.getEnable()){
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("id", invoiceTitle.getId());
				map.put("title", invoiceTitle.getTitle());
				list.add(map);
			}
		}
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("items", list);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}

}
