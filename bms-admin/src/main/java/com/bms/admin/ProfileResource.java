/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.admin.pojo.Profile;
import com.bms.admin.service.ProfileService;
import com.bms.common.ConfigUtil;
import com.bms.common.http.ErrorResponse;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.security.core.SecurityContextHolder;

/**
 * @author wangjian
 * @create 2013年8月29日 下午8:55:51
 * @update TODO
 * 
 * 
 */
@Path("/profiles")
@Controller
public class ProfileResource extends BaseResource{
	private static final DateFormat birthDayformat = new SimpleDateFormat("yyyy-MM-dd"); 
	
	@Autowired
	private ProfileService profileService;
	
	@Path("/add") @POST
	@Produces(MediaType.APPLICATION_JSON)
	public String addProfile(@FormParam("real_name") String realname, @FormParam("age") Integer age,
			@FormParam("nationality") String nationality, @FormParam("language") String language, 
			@FormParam("gender") String gender, @FormParam("birthday") String birthday, 
			@FormParam("idtype") String idtype, @FormParam("idnum") String idnum, 
			@FormParam("department") String department, @FormParam("position") String position, 
			@FormParam("address") String address, @FormParam("description") String description) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Profile profile = new Profile();
		
		profile.setAccount_id(uid);
		if (realname != null && !realname.equals("")) profile.setRealname(realname);
		if (age != null && age != 0) profile.setAge(age);
		if (nationality != null && !nationality.equals("")) profile.setNationality(nationality);
		if (language != null && !language.equals("")) profile.setLanguage(language);
		if (gender != null && !gender.equals("")) profile.setGender(gender);
		if (birthday != null && !birthday.equals("")) {
			try {
				java.sql.Date birth = new java.sql.Date(birthDayformat.parse(birthday).getTime());
				profile.setBirthday(birth);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (idtype != null && !idtype.equals("")) profile.setIdtype(idtype);
		if (idnum != null && !idnum.equals("")) profile.setIdnum(idnum);
		if (department != null && !department.equals("")) profile.setDepartment(department);
		if (position != null && !position.equals("")) profile.setPosition(position);
		if (address != null && !address.equals("")) profile.setAddress(address);
		if (description != null && !description.equals("")) profile.setDescription(description);
		profile.setLast_modify_person(uid);
		profile.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		
		profileService.insertProfile(profile);
		
		SuccessResponse response = new SuccessResponse(10000, "Add Profile Successfully");
		
		return response.toJson();
	}
	
	@Path("/update") @POST
	@Produces(MediaType.APPLICATION_JSON)
	public String updateProfile(@FormParam("real_name") String realname, @FormParam("age") Integer age,
			@FormParam("nationality") String nationality, @FormParam("language") String language, 
			@FormParam("gender") String gender, @FormParam("birthday") String birthday, 
			@FormParam("idtype") String idtype, @FormParam("idnum") String idnum, 
			@FormParam("department") String department, @FormParam("position") String position, 
			@FormParam("address") String address, @FormParam("description") String description) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Profile profile = profileService.queryProfile(uid);
		
		if (realname != null && !realname.equals("")) profile.setRealname(realname);
		if (age != null && age != 0) profile.setAge(age);
		if (nationality != null && !nationality.equals("")) profile.setNationality(nationality);
		if (language != null && !language.equals("")) profile.setLanguage(language);
		if (gender != null && !gender.equals("")) profile.setGender(gender);
		if (birthday != null && !birthday.equals("")) {
			try {
				java.sql.Date birth = new java.sql.Date(birthDayformat.parse(birthday).getTime());
				profile.setBirthday(birth);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (idtype != null && !idtype.equals("")) profile.setIdtype(idtype);
		if (idnum != null && !idnum.equals("")) profile.setIdnum(idnum);
		if (department != null && !department.equals("")) profile.setDepartment(department);
		if (position != null && !position.equals("")) profile.setPosition(position);
		if (address != null && !address.equals("")) profile.setAddress(address);
		if (description != null && !description.equals("")) profile.setDescription(description);
		profile.setLast_modify_person(uid);
		profile.setLast_modify_time(new Timestamp(System.currentTimeMillis()));
		
		profileService.updateProfile(profile);
		
		SuccessResponse response = new SuccessResponse(10000, "Update Profile Successfully");
		
		return response.toJson();
	}
	
	@SuppressWarnings("unchecked")
	@Path("/view") @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getProfile(@QueryParam("account_id") Integer account_id) {
		Integer uid;
		if (account_id == null) {
			uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		} else {
			uid = account_id;
		}
		Profile profile = profileService.queryProfile(uid);
		if (profile == null) {
			String request = uriInfo.getRequestUri().toString();
			ErrorResponse response = new ErrorResponse(request, "20701", ConfigUtil.getValue("20701"));
			return response.toJson();
		}
		
		NormalResponse response = new NormalResponse();
		response.setAttrs(profile.toMap());
		
		return response.toJson();
	}
}
