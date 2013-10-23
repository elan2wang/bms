/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.common.consts;

/**
 * @author wangjian
 * @create 2013年8月6日 下午7:31:35
 * @update TODO
 * 
 * 
 */
public class State {
	
	/**
	 * PushMessage states
	 */
	public static String PUSH_MESSAGE_NEW = "new";
	public static String PUSH_MESSAGE_READ = "read";
	
	/**
	 * Payments stated
	 */
	public static String PAYMENT_NEW = "new";
	public static String PAYMENT_PENDING = "pending";
	public static String PAYMENT_AUDIT_AGREED = "agreed";
	public static String PAYMENT_AUDIT_DISAGREED = "disagreed";
	public static String PAYMENT_ACCOUNTANT_PAYED = "accountant_payed";
	public static String PAYMENT_TELLER_PAYED = "teller_payed";
	public static String PAYMENT_INVOICE_OPEN = "open";
	public static String PAYMENT_INVOICE_CLOSED = "closed";
	public static String PAYMENT_CLOSED = "closed";
	
	/**
	 * Income states
	 */
	public static String INCOME_NEW = "new";
	public static String INCOME_AUDITED = "audited";
	public static String INCOME_PENDING = "pending";
	public static String INCOME_CLOSED = "closed";
	public static String INCOME_INVOICE_OPEN = "open";
	public static String INCOME_INVOICE_CLOSED = "closed";
	
	/**
	 * Invoice types for income
	 */
	public static String INCOME_INVOICE_NO = "invoice_no";
	public static String INCOME_INVOICE_FIRST = "invoice_first";
	public static String INCOME_INVOICE_LAST = "invoice_last";
	
	/**
	 * Bring types for income
	 */
	public static String INCOME_DEPOSIT = "deposit";
	public static String INCOME_TRANSFER = "transfer";
	
	/**
	 * Task state
	 */
	public static String TASK_NOT_START = "not_start";
	public static String TASK_ONGOING = "ongoing";
	public static String TASK_FINISHED = "finished";
	
	/**
	 * Subtask state
	 */
	public static String SUBTASK_NOT_START = "not_start";
	public static String SUBTASK_ONGOING = "ongoing";
	public static String SUBTASK_FINISHED = "finished";
	
	/**
	 * SubtaskItem state
	 */
	public static String SUBTASK_ITEM_NOT_START = "not_start";
	public static String SUBTASK_ITEM_CONFIRMED = "confirmed";
	public static String SUBTASK_ITEM_FINISHED = "finished";

}
