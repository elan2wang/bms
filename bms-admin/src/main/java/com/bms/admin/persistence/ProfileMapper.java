/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.Profile;

/**
 * @author wangjian
 * @create 2013年8月15日 下午4:01:47
 * @update TODO
 * 
 * 
 */
public interface ProfileMapper {
	
	public void insertProfile(Profile profile);
	
	public void updateProfile(Profile profile);
	
	public void deleteProfile(Integer profile_id);
	
	@SuppressWarnings("rawtypes")
	public Profile queryProfile(Map params);
	
	//获得所有的Profile
	public List<Profile> queryProfiles();
	
}
