/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.common;

import java.security.MessageDigest;

/**
 * @author wangjian
 * @create 2013年8月4日 下午8:15:56
 * @update TODO
 * 
 */
public class Crypto {
	private static final String password = "jsiuewiu6%&*&8fjkaklsalsd38%^fha";
	private static final char hexDigits[] = 
		{ '0', '1', '2', '3', '4', 
		  '5', '6', '7', '8', '9', 
		  'A', 'B', 'C', 'D', 'E', 'F'
		}; 
	
	public final static String MD5Encrypt(String s) { 
		
		try 
		{ 
			byte[] btInput = s.getBytes(); 
			MessageDigest mdInst = MessageDigest.getInstance("MD5"); 
			mdInst.update(password.getBytes());
			mdInst.update(btInput); 
			byte[] md = mdInst.digest(); 
			int len = md.length; 
			char str[] = new char[len * 2]; 
			int j = 0;
			for (int i = 0; i < len; i++) { 
				byte byte0 = md[i]; 
				str[j++] = hexDigits[byte0 >>> 4 & 0xf]; 
				str[j++] = hexDigits[byte0 & 0xf]; 
			} 
			return new String(str); 
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
