package com.stock.vo;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;

public class HbaseColumnValueVo {
	
	   private String family;
	   
	   private String column;
	   
	   private String value;
	   
//	   /** less than */
//	    LESS,   1
//	    /** less than or equal to */
//	    LESS_OR_EQUAL,  2
//	    /** equals */
//	    EQUAL,   3
//	    /** not equal */
//	    NOT_EQUAL,   4
//	    /** greater than or equal to */
//	    GREATER_OR_EQUAL,   5
//	    /** greater than */
//	    GREATER,  6
//	    /** no operation */
//	    NO_OP,   7
	   private String compareValue;

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CompareOp getOp() {
		if(compareValue.equals("1")){
			return CompareOp.LESS;
		}else if(compareValue.equals("2")){
			return CompareOp.LESS_OR_EQUAL;
		}else if(compareValue.equals("3")){
			return CompareOp.EQUAL;
		}else if(compareValue.equals("4")){
			return CompareOp.NOT_EQUAL;
		}else if(compareValue.equals("5")){
			return CompareOp.GREATER_OR_EQUAL;
		}else if(compareValue.equals("6")){
			return CompareOp.GREATER;
		}else if(compareValue.equals("7")){
			return CompareOp.NO_OP;
		}else {
			return CompareOp.NOT_EQUAL;
		}
	}

	public void setOp(String value) {
		this.compareValue = value;
	}
	   
	
	   
		
		
	    
	    
	    

}
