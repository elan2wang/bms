/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.admin.persistence.ProfileMapper;
import com.bms.admin.pojo.Profile;

/**
 * @author wangjian
 * @create 2013年8月15日 下午4:05:01
 * @update TODO
 * 
 * 
 */
@Service
public class ProfileService {
	
	@Autowired
	private ProfileMapper profileMapper;
	
	@Transactional
	public void insertProfile(Profile profile) {
		profileMapper.insertProfile(profile);
	}
	
	@Transactional
	public void updateProfile(Profile profile) {
		profileMapper.updateProfile(profile);
	}
	
	@Transactional
	public void deleteProfile(Integer profile_id) {
		profileMapper.deleteProfile(profile_id);
	}
	
	public Profile queryProfile(Integer account_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_id", account_id);
		return profileMapper.queryProfile(params);
	}
	
	//获得所有的Profile
	public List<Profile> queryProfiles(){
		return profileMapper.queryProfiles();
	}
}
