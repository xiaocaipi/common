package com.stock.vo;

import java.io.Serializable;

public class StockTimeSharingVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String rowkey;
	
	private String code;

	private String minutes;

	private Double close;

	private Double dealSumShou;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Double getDealSumShou() {
		return dealSumShou;
	}

	public void setDealSumShou(Double dealSumShou) {
		this.dealSumShou = dealSumShou;
	}

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}
	
	

}
