package com.stock.vo;

import java.io.Serializable;

public class StockAlertVo extends StockData implements Serializable {
	

	private String overprice;
	
	private String lowprice;
	
	private String zhangfu;
	
	private String diefu;
	
	//1今天已经预警过  2今天没有预警过
	private String status;

	public String getOverprice() {
		return overprice;
	}

	public void setOverprice(String overprice) {
		this.overprice = overprice;
	}

	public String getLowprice() {
		return lowprice;
	}

	public void setLowprice(String lowprice) {
		this.lowprice = lowprice;
	}

	public String getZhangfu() {
		return zhangfu;
	}

	public void setZhangfu(String zhangfu) {
		this.zhangfu = zhangfu;
	}

	public String getDiefu() {
		return diefu;
	}

	public void setDiefu(String diefu) {
		this.diefu = diefu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	

}
