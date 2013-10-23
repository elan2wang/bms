/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.bms.common.consts.State;

/**
 * @author asus
 * @create 2013年8月14日 下午11:10:49
 * @update TODO
 * 
 * 
 */
public class Income implements Serializable{

	private static final long serialVersionUID = -7288791140499868608L;
	
	private Integer id;
	private String title;
	private Float money;
	private Integer income_type;
	private String bring_type;
	private String bank_card;
	private String serial_num;
	private String invoice_type;
	private String invoice_title;
	private Timestamp generate_time;
	private Timestamp create_time;
	private Timestamp audit_time;
	private Timestamp invoice_time;
	private Timestamp handle_time;
	private Integer creator_id;
	private Integer teller_id;
	private Integer accountant_id;
	private Integer manager_id;
	private Integer department_id;
	private String creator_comment;
	private String teller_comment;
	private String accountant_comment;
	private String manager_comment;
	private String invoice_state;
	private String state;
	private Timestamp last_modify_time;
	private Integer last_modify_person;
	
	public Income() {
		super();
	}

	public Income(Integer id, String title, Float money, Integer income_type,
			String bring_type, String bank_card, String serial_num,
			String invoice_type, String invoice_title, Timestamp generate_time,
			Timestamp create_time, Timestamp audit_time,
			Timestamp invoice_time, Timestamp handle_time, Integer creator_id,
			Integer teller_id, Integer accountant_id, Integer manager_id,
			Integer department_id, String creator_comment,
			String teller_comment, String accountant_comment,
			String manager_comment, String invoice_state, String state,
			Timestamp last_modify_time, Integer last_modify_person) {
		super();
		this.id = id;
		this.title = title;
		this.money = money;
		this.income_type = income_type;
		this.bring_type = bring_type;
		this.bank_card = bank_card;
		this.serial_num = serial_num;
		this.invoice_type = invoice_type;
		this.invoice_title = invoice_title;
		this.generate_time = generate_time;
		this.create_time = create_time;
		this.audit_time = audit_time;
		this.invoice_time = invoice_time;
		this.handle_time = handle_time;
		this.creator_id = creator_id;
		this.teller_id = teller_id;
		this.accountant_id = accountant_id;
		this.manager_id = manager_id;
		this.department_id = department_id;
		this.creator_comment = creator_comment;
		this.teller_comment = teller_comment;
		this.accountant_comment = accountant_comment;
		this.manager_comment = manager_comment;
		this.invoice_state = invoice_state;
		this.state = state;
		this.last_modify_time = last_modify_time;
		this.last_modify_person = last_modify_person;
	}
	
	public Income(String title, Float money, Integer income_type,
			String bring_type, String bank_card, String serial_num,
			String invoice_type, String invoice_title, Timestamp generate_time,
			Integer creator_id,Integer department_id, String creator_comment) {
		super();
		this.title = title;
		this.money = money;
		this.income_type = income_type;
		this.bring_type = bring_type;
		this.bank_card = bank_card;
		this.serial_num = serial_num;
		this.invoice_type = invoice_type;
		this.invoice_title = invoice_title;
		this.generate_time = generate_time;
		this.create_time = new Timestamp(System.currentTimeMillis());
		this.audit_time = null;
		this.invoice_time = null;
		this.handle_time = null;
		this.creator_id = creator_id;
		this.teller_id = null;
		this.accountant_id = null;
		this.manager_id = null;
		this.department_id = department_id;
		this.creator_comment = creator_comment;
		this.teller_comment = null;
		this.accountant_comment = null;
		this.manager_comment = null;
		if(this.invoice_type.equals("invoice_no")){
			this.invoice_state = State.INCOME_INVOICE_CLOSED;
		}else{
			this.invoice_state = State.INCOME_INVOICE_OPEN;
		}
		this.state = State.INCOME_NEW;
		this.last_modify_time = this.create_time;
		this.last_modify_person = this.creator_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Integer getIncome_type() {
		return income_type;
	}

	public void setIncome_type(Integer income_type) {
		this.income_type = income_type;
	}

	public String getBring_type() {
		return bring_type;
	}

	public void setBring_type(String bring_type) {
		this.bring_type = bring_type;
	}

	public String getBank_card() {
		return bank_card;
	}

	public void setBank_card(String bank_card) {
		this.bank_card = bank_card;
	}

	public String getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
	}

	public String getInvoice_type() {
		return invoice_type;
	}

	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}

	public String getInvoice_title() {
		return invoice_title;
	}

	public void setInvoice_title(String invoice_title) {
		this.invoice_title = invoice_title;
	}

	public Timestamp getGenerate_time() {
		return generate_time;
	}

	public void setGenerate_time(Timestamp generate_time) {
		this.generate_time = generate_time;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getAudit_time() {
		return audit_time;
	}

	public void setAudit_time(Timestamp audit_time) {
		this.audit_time = audit_time;
	}

	public Timestamp getInvoice_time() {
		return invoice_time;
	}

	public void setInvoice_time(Timestamp invoice_time) {
		this.invoice_time = invoice_time;
	}

	public Timestamp getHandle_time() {
		return handle_time;
	}

	public void setHandle_time(Timestamp handle_time) {
		this.handle_time = handle_time;
	}

	public Integer getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(Integer creator_id) {
		this.creator_id = creator_id;
	}

	public Integer getTeller_id() {
		return teller_id;
	}

	public void setTeller_id(Integer teller_id) {
		this.teller_id = teller_id;
	}

	public Integer getAccountant_id() {
		return accountant_id;
	}

	public void setAccountant_id(Integer accountant_id) {
		this.accountant_id = accountant_id;
	}

	public Integer getManager_id() {
		return manager_id;
	}

	public void setManager_id(Integer manager_id) {
		this.manager_id = manager_id;
	}

	public Integer getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(Integer department_id) {
		this.department_id = department_id;
	}

	public String getCreator_comment() {
		return creator_comment;
	}

	public void setCreator_comment(String creator_comment) {
		this.creator_comment = creator_comment;
	}

	public String getTeller_comment() {
		return teller_comment;
	}

	public void setTeller_comment(String teller_comment) {
		this.teller_comment = teller_comment;
	}

	public String getAccountant_comment() {
		return accountant_comment;
	}

	public void setAccountant_comment(String accountant_comment) {
		this.accountant_comment = accountant_comment;
	}

	public String getManager_comment() {
		return manager_comment;
	}

	public void setManager_comment(String manager_comment) {
		this.manager_comment = manager_comment;
	}

	public String getInvoice_state() {
		return invoice_state;
	}

	public void setInvoice_state(String invoice_state) {
		this.invoice_state = invoice_state;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	@Override
	public String toString() {
		return "Income [id=" + id + ", title=" + title + ", money=" + money
				+ ", income_type=" + income_type + ", bring_type=" + bring_type
				+ ", bank_card=" + bank_card + ", serial_num=" + serial_num
				+ ", invoice_type=" + invoice_type + ", invoice_title="
				+ invoice_title + ", generate_time=" + generate_time
				+ ", create_time=" + create_time + ", audit_time=" + audit_time
				+ ", invoice_time=" + invoice_time + ", handle_time="
				+ handle_time + ", creator_id=" + creator_id + ", teller_id="
				+ teller_id + ", accountant_id=" + accountant_id
				+ ", manager_id=" + manager_id + ", department_id="
				+ department_id + ", creator_comment=" + creator_comment
				+ ", teller_comment=" + teller_comment
				+ ", accountant_comment=" + accountant_comment
				+ ", manager_comment=" + manager_comment + ", invoice_state="
				+ invoice_state + ", state=" + state + ", last_modify_time="
				+ last_modify_time + ", last_modify_person="
				+ last_modify_person + "]";
	}
	
}
