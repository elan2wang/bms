/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author AI
 * @create 2013-9-18 下午3:35:09
 * @update TODO
 * 
 * 
 */
public class Vault implements Serializable {

	private static final long serialVersionUID = 8604481395591261623L;
	
	private String v_number;
	private String type;
	private String alias;
	private String comment;
	private String card_bank;
	private String card_owner;
	private Integer payment_count;
	private Integer income_count;
	private Timestamp create_time;
	private Timestamp last_modify_time;
	private Integer last_modify_person;
	private Boolean enable;
	
	public Vault() {
		super();
	}

	public Vault(String v_number, String type, String alias, String comment,
			String card_bank, String card_owner, Integer payment_count,
			Integer income_count, Timestamp create_time,
			Timestamp last_modify_time, Integer last_modify_person, Boolean enable) {
		super();
		this.v_number = v_number;
		this.type = type;
		this.alias = alias;
		this.comment = comment;
		this.card_bank = card_bank;
		this.card_owner = card_owner;
		this.payment_count = payment_count;
		this.income_count = income_count;
		this.create_time = create_time;
		this.last_modify_time = last_modify_time;
		this.last_modify_person = last_modify_person;
		this.enable = enable;
	}

	public Vault(String v_number, String type, String alias) {
		super();
		this.v_number = v_number;
		this.type = type;
		this.alias = alias;
		this.comment = null;
		this.card_bank = null;
		this.card_owner = null;
		this.payment_count = 0;
		this.income_count = 0;
		this.create_time = new Timestamp(System.currentTimeMillis());
		this.last_modify_time = this.create_time;
		this.last_modify_person = null;
		this.enable = true;
	}
	
	
	
	public Vault(String v_number, String type, String alias, String comment,
			String card_bank, String card_owner, Integer last_modify_person) {
		super();
		this.v_number = v_number;
		this.type = type;
		this.alias = alias;
		this.comment = comment;
		this.card_bank = card_bank;
		this.card_owner = card_owner;
		this.payment_count = 0;
		this.income_count = 0;
		this.create_time = new Timestamp(System.currentTimeMillis());
		this.last_modify_time = create_time;
		this.last_modify_person = last_modify_person;
		this.enable = true;
	}

	public String getV_number() {
		return v_number;
	}

	public void setV_number(String v_number) {
		this.v_number = v_number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCard_bank() {
		return card_bank;
	}

	public void setCard_bank(String card_bank) {
		this.card_bank = card_bank;
	}

	public String getCard_owner() {
		return card_owner;
	}

	public void setCard_owner(String card_owner) {
		this.card_owner = card_owner;
	}

	public Integer getPayment_count() {
		return payment_count;
	}

	public void setPayment_count(Integer payment_count) {
		this.payment_count = payment_count;
	}

	public Integer getIncome_count() {
		return income_count;
	}

	public void setIncome_count(Integer income_count) {
		this.income_count = income_count;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getLast_modify_time() {
		return last_modify_time;
	}

	public void setLast_modify_time(Timestamp last_modify_time) {
		this.last_modify_time = last_modify_time;
	}

	public Integer getLast_modify_person() {
		return last_modify_person;
	}

	public void setLast_modify_person(Integer last_modify_person) {
		this.last_modify_person = last_modify_person;
	}

	public Boolean getEnable(){
		return enable;
	}
	
	public void setEnable(Boolean enable){
		this.enable = enable;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Vault [v_number=" + v_number + ", type=" + type + ", alias="
				+ alias + ", comment=" + comment + ", card_bank=" + card_bank
				+ ", card_owner=" + card_owner + ", payment_count="
				+ payment_count + ", income_count=" + income_count
				+ ", create_time=" + create_time + ", last_modify_time="
				+ last_modify_time + ", last_modify_person="
				+ last_modify_person + ", enable=" + enable + "]";
	}
	
	@SuppressWarnings("rawtypes")
	public Map toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("v_number", v_number);
		map.put("type", type);
		map.put("alias", alias);
		map.put("comment", comment);
		map.put("card_bank", card_bank);
		map.put("card_owner", card_owner);
		map.put("payment_count", payment_count);
		map.put("income_count", income_count);
		map.put("create_time", create_time);
		map.put("last_modify_person", last_modify_person);
		map.put("last_modify_time", last_modify_time);
		map.put("enable", enable);
		return map;
	}
	

	

	
	
}
