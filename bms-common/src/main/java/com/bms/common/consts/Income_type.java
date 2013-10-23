/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.common.consts;

/**
 * @author asus
 * @create 2013年8月23日 下午3:49:50
 * @update TODO
 * 
 * 
 */
public enum Income_type {
	
	turnover {
		public String toString(){
			return "营业额";
		}
	}, 
	training {
		public String toString(){
			return "培训收入";
		}
	}, 
	balance {
		public String toString(){
			return "余款退回";
		}
	}, 
	debt {
		public String toString(){
			return "应收债款";
		}
	}, 
	investment {
		public String toString(){
			return "投资";
		}
	},
	other {
		public String toString(){
			return "其它";
		}
	};
	
	public static Income_type myValueOf(Integer type){
		
		switch(type){
		case 0 : return Income_type.turnover;
		case 1 : return Income_type.training;
		case 2 : return Income_type.balance;
		case 3 : return Income_type.debt;
		case 4 : return Income_type.investment;
		default : return Income_type.other;
		}
		
	}
	
}
