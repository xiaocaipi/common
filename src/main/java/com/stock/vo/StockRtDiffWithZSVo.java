package com.stock.vo;

import java.io.Serializable;
import java.text.DecimalFormat;

public class StockRtDiffWithZSVo implements Serializable{
	
	
	
	
	public StockRtDiffWithZSVo(String code, String time, String batch_id, double diff_sum_3, double diff_sum_6,
			double diff_sum_9, double diff_sum_21, double diff_sum_30) {
		super();
		this.code = code;
		this.time = time;
		this.batch_id = batch_id;
		this.diff_sum_3 = diff_sum_3;
		this.diff_sum_6 = diff_sum_6;
		this.diff_sum_9 = diff_sum_9;
		this.diff_sum_21 = diff_sum_21;
		this.diff_sum_30 = diff_sum_30;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String rowkey;

	private String code ;
	
	private String time;
	
	private String batch_id;
	
	private Double diff_sum_3;
	
	private Double diff_sum_6;
	
	private Double diff_sum_9;
	
	private Double diff_sum_21;
	
	private Double diff_sum_30;
	
	private Double zhangdiefudu;
	
	private Double diff_60;

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}

	public Double getDiff_sum_3() {
		return diff_sum_3;
	}

	public void setDiff_sum_3(Double diff_sum_3) {
		this.diff_sum_3 = diff_sum_3;
	}

	public Double getDiff_sum_6() {
		return diff_sum_6;
	}

	public void setDiff_sum_6(Double diff_sum_6) {
		this.diff_sum_6 = diff_sum_6;
	}

	public Double getDiff_sum_9() {
		return diff_sum_9;
	}

	public void setDiff_sum_9(Double diff_sum_9) {
		this.diff_sum_9 = diff_sum_9;
	}

	public Double getDiff_sum_21() {
		return diff_sum_21;
	}

	public void setDiff_sum_21(Double diff_sum_21) {
		this.diff_sum_21 = diff_sum_21;
	}

	public Double getDiff_sum_30() {
		return diff_sum_30;
	}

	public void setDiff_sum_30(Double diff_sum_30) {
		this.diff_sum_30 = diff_sum_30;
	}
	
	

	
	public Double getZhangdiefudu() {
		return zhangdiefudu;
	}

	public void setZhangdiefudu(Double zhangdiefudu) {
		this.zhangdiefudu = zhangdiefudu;
	}
	
	

	public Double getDiff_60() {
		return diff_60;
	}

	public void setDiff_60(Double diff_60) {
		this.diff_60 = diff_60;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.0000");
		String out = this.code+"--"+this.batch_id+"--"+this.time+"--   "+df.format(this.diff_sum_3)
					 +"--   "+df.format(this.zhangdiefudu)+"--   "+df.format(this.diff_60);
		return out;
	}
	
	
	
	
	
	
	
	

}
