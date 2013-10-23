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
 * @author AI
 * @create 2013年8月12日 下午7:55:08
 * @update TODO
 * 
 * 
 */
public class Payment implements Serializable {

	private static final long serialVersionUID = -7481641775548568708L;

	private Integer id;
	private String title;
	private Float money;
	private Boolean invoice_need;
	private String invoice_title;
	private String pay_type;
	private String src_account;
	private String dst_card_num;
	private String dst_bank_name;
	private String dst_account_name;
	private Boolean urgency;
	private Timestamp apply_time;
    private Timestamp audit_time;
	private Timestamp accountant_pay_time;
    private Timestamp teller_pay_time;
    private Timestamp invoice_time;
	private Integer applicant_id;
	private Integer teller_id;
	private Integer manager_id;
	private Integer accountant_id;
	private Integer department_id;
	private String applicant_comment;
	private String teller_comment;
	private String manager_comment;
	private String accountant_comment;
	private String invoice_state;
    private String state;
    private Timestamp last_modify_time;
	private Integer last_modify_person; 
	
    public Payment() {
		super();
	}
    
	public Payment(Integer id, String title, float money, Boolean invoice_need,
			String invoice_title, String pay_type, String src_account,
			String dst_card_num, String dst_bank_name, String dst_account_name,
			Boolean urgency, Timestamp apply_time, Timestamp audit_time,
			Timestamp accountant_pay_time, Timestamp teller_pay_time,
			Timestamp invoice_time, Integer applicant_id, Integer teller_id,
			Integer manager_id, Integer accountant_id, Integer department_id,
			String applicant_comment, String teller_comment,
			String manager_comment, String accountant_comment,
			String invoice_state, String state, Timestamp last_modify_time,
			Integer last_modify_person) {
		super();
		this.id = id;
		this.title = title;
		this.money = money;
		this.invoice_need = invoice_need;
		this.invoice_title = invoice_title;
		this.pay_type = pay_type;
		this.src_account = src_account;
		this.dst_card_num = dst_card_num;
		this.dst_bank_name = dst_bank_name;
		this.dst_account_name = dst_account_name;
		this.urgency = urgency;
		this.apply_time = apply_time;
		this.audit_time = audit_time;
		this.accountant_pay_time = accountant_pay_time;
		this.teller_pay_time = teller_pay_time;
		this.invoice_time = invoice_time;
		this.applicant_id = applicant_id;
		this.teller_id = teller_id;
		this.manager_id = manager_id;
		this.accountant_id = accountant_id;
		this.department_id = department_id;
		this.applicant_comment = applicant_comment;
		this.teller_comment = teller_comment;
		this.manager_comment = manager_comment;
		this.accountant_comment = accountant_comment;
		this.invoice_state = invoice_state;
		this.state = state;
		this.last_modify_time = last_modify_time;
		this.last_modify_person = last_modify_person;
	}

	public Payment(String title, float money, Boolean invoice_need,
			String pay_type, String dst_card_num, String dst_bank_name, String dst_account_name,
			Boolean urgency, Integer applicant_id, Integer department_id, String applicant_comment) {
		super();
		this.title = title;
		this.money = money;
		this.invoice_need = invoice_need;
		this.pay_type = pay_type;
		this.dst_card_num = dst_card_num;
		this.dst_bank_name = dst_bank_name;
		this.dst_account_name = dst_account_name;
		this.urgency = urgency;
		this.apply_time = new Timestamp(System.currentTimeMillis());
		this.applicant_id = applicant_id;
		this.department_id = department_id;
		this.applicant_comment = applicant_comment;
		this.invoice_state = State.PAYMENT_INVOICE_OPEN;
		this.state = State.PAYMENT_NEW;
		this.last_modify_time = this.apply_time;
		this.last_modify_person = this.applicant_id;
		this.invoice_title = null;
		this.src_account = null;
		this.audit_time = null;
		this.accountant_pay_time = null;
		this.teller_pay_time = null;
		this.invoice_time = null;
		this.teller_id = null;
		this.manager_id = null;
		this.accountant_id = null;
		this.teller_comment = null;
		this.manager_comment = null;
		this.accountant_comment = null;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApplicant_id() {
		return applicant_id;
	}

	public void setApplicant_id(Integer applicant_id) {
		this.applicant_id = applicant_id;
	}

	public Integer getTeller_id() {
		return teller_id;
	}

	public void setTeller_id(Integer teller_id) {
		this.teller_id = teller_id;
	}

	public Integer getManager_id() {
		return manager_id;
	}

	public void setManager_id(Integer manager_id) {
		this.manager_id = manager_id;
	}

	public Integer getAccountant_id() {
		return accountant_id;
	}

	public void setAccountant_id(Integer accountant_id) {
		this.accountant_id = accountant_id;
	}

	public Integer getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(Integer department_id) {
		this.department_id = department_id;
	}

	public Integer getLast_modify_person() {
		return last_modify_person;
	}

	public void setLast_modify_person(Integer last_modify_person) {
		this.last_modify_person = last_modify_person;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInvoice_title() {
		return invoice_title;
	}

	public void setInvoice_title(String invoice_title) {
		this.invoice_title = invoice_title;
	}

	public String getSrc_account() {
		return src_account;
	}

	public void setSrc_account(String src_account) {
		this.src_account = src_account;
	}

	public String getDst_card_num() {
		return dst_card_num;
	}

	public void setDst_card_num(String dst_card_num) {
		this.dst_card_num = dst_card_num;
	}

	public String getDst_bank_name() {
		return dst_bank_name;
	}

	public void setDst_bank_name(String dst_bank_name) {
		this.dst_bank_name = dst_bank_name;
	}

	public String getDst_account_name() {
		return dst_account_name;
	}

	public void setDst_account_name(String dst_account_name) {
		this.dst_account_name = dst_account_name;
	}

	public String getApplicant_comment() {
		return applicant_comment;
	}

	public void setApplicant_comment(String applicant_comment) {
		this.applicant_comment = applicant_comment;
	}

	public String getTeller_comment() {
		return teller_comment;
	}

	public void setTeller_comment(String teller_comment) {
		this.teller_comment = teller_comment;
	}

	public String getManager_comment() {
		return manager_comment;
	}

	public void setManager_comment(String manager_comment) {
		this.manager_comment = manager_comment;
	}

	public String getAccountant_comment() {
		return accountant_comment;
	}

	public void setAccountant_comment(String accountant_comment) {
		this.accountant_comment = accountant_comment;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
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

	public Timestamp getApply_time() {
		return apply_time;
	}

	public void setApply_time(Timestamp apply_time) {
		this.apply_time = apply_time;
	}

	public Timestamp getAudit_time() {
		return audit_time;
	}

	public void setAudit_time(Timestamp audit_time) {
		this.audit_time = audit_time;
	}

	public Timestamp getAccountant_pay_time() {
		return accountant_pay_time;
	}

	public void setAccountant_pay_time(Timestamp accountant_pay_time) {
		this.accountant_pay_time = accountant_pay_time;
	}

	public Timestamp getTeller_pay_time() {
		return teller_pay_time;
	}

	public void setTeller_pay_time(Timestamp teller_pay_time) {
		this.teller_pay_time = teller_pay_time;
	}

	public Timestamp getInvoice_time() {
		return invoice_time;
	}

	public void setInvoice_time(Timestamp invoice_time) {
		this.invoice_time = invoice_time;
	}

	public Timestamp getLast_modify_time() {
		return last_modify_time;
	}

	public void setLast_modify_time(Timestamp last_modify_time) {
		this.last_modify_time = last_modify_time;
	}

	public Boolean getInvoice_need() {
		return invoice_need;
	}

	public void setInvoice_need(Boolean invoice_need) {
		this.invoice_need = invoice_need;
	}

	public Boolean getUrgency() {
		return urgency;
	}

	public void setUrgency(Boolean urgency) {
		this.urgency = urgency;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", applicant_id=" + applicant_id
				+ ", teller_id=" + teller_id + ", manager_id=" + manager_id
				+ ", accountant_id=" + accountant_id + ", department_id="
				+ department_id + ", last_modify_person=" + last_modify_person
				+ ", money=" + money + ", title=" + title + ", invoice_title="
				+ invoice_title + ", src_account=" + src_account
				+ ", dst_card_num=" + dst_card_num + ", dst_bank_name="
				+ dst_bank_name + ", dst_account_name=" + dst_account_name
				+ ", applicant_comment=" + applicant_comment
				+ ", teller_comment=" + teller_comment + ", manager_comment="
				+ manager_comment + ", accountant_comment="
				+ accountant_comment + ", pay_type=" + pay_type
				+ ", invoice_state=" + invoice_state + ", state=" + state
				+ ", apply_time=" + apply_time + ", audit_time=" + audit_time
				+ ", accountant_pay_time=" + accountant_pay_time
				+ ", teller_pay_time=" + teller_pay_time + ", invoice_time="
				+ invoice_time + ", last_modify_time=" + last_modify_time
				+ ", invoice_need=" + invoice_need + ", urgency=" + urgency
				+ "]";
	}
	
	
    
}
