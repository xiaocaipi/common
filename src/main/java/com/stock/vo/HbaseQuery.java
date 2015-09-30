package com.stock.vo;

import java.util.List;

public class HbaseQuery {
	
	    private String rowkey ;

	    private String rowkeyPrefix;

	    private int querySize;

	    private String startKey;

	    private String stopKey;

	    private boolean reverved;
	    
	    private List<HbaseColumnValueVo> columValueList;
	    
	    private String type;

		public String getRowkey() {
			return rowkey;
		}

		public void setRowkey(String rowkey) {
			this.rowkey = rowkey;
		}

		public String getRowkeyPrefix() {
			return rowkeyPrefix;
		}

		public void setRowkeyPrefix(String rowkeyPrefix) {
			this.rowkeyPrefix = rowkeyPrefix;
		}

		public int getQuerySize() {
			return querySize;
		}

		public void setQuerySize(int querySize) {
			this.querySize = querySize;
		}

		public String getStartKey() {
			return startKey;
		}

		public void setStartKey(String startKey) {
			this.startKey = startKey;
		}

		public String getStopKey() {
			return stopKey;
		}

		public void setStopKey(String stopKey) {
			this.stopKey = stopKey;
		}

		public boolean isReverved() {
			return reverved;
		}

		public void setReverved(boolean reverved) {
			this.reverved = reverved;
		}


		public List<HbaseColumnValueVo> getColumValueList() {
			return columValueList;
		}

		public void setColumValueList(List<HbaseColumnValueVo> columValueList) {
			this.columValueList = columValueList;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
		
	    
	    
	    

}
