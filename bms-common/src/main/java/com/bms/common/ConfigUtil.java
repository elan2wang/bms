/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


/**
 * @author wangjian
 * @create 2013年7月29日 下午8:03:36
 * @update TODO
 * 
 * 
 */
public class ConfigUtil {

	private static Properties appProperties = null;

	static {
		appProperties = new Properties();
		InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		try {
			appProperties.load(inputStream1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		InputStream inputStream2 = null;
		InputStream inputStream3 = null;
		if (getValue("language") == null) {
			inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("en_error_msg.properties");
			inputStream3 = Thread.currentThread().getContextClassLoader().getResourceAsStream("en_success_msg.properties");
		} else if (getValue("language").equals("Chinese")) {
			inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("cn_error_msg.properties");
			inputStream3 = Thread.currentThread().getContextClassLoader().getResourceAsStream("cn_success_msg.properties");
		} else {
			inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("en_error_msg.properties");
			inputStream3 = Thread.currentThread().getContextClassLoader().getResourceAsStream("en_success_msg.properties");
		}
		
		try {
			appProperties.load(inputStream2);
			appProperties.load(inputStream3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key)
	{
		String value = (String) appProperties.get(key);	
		if (value != null){
			try {
				return new String(value.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
		
	}
}
