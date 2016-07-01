package service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import com.stock.vo.HbaseColumnValueVo;
import com.stock.vo.HbaseQuery;

import util.CommonUtil;

@Service
public abstract class BaseHbaseService {
	
	/**
    *condition  map 
    *type  1 是dev  2是测试   default   1
    *rowkey_prefix   
    *querySize  
    *reversed     
    *start_row
    *stop_row
    *column_value   用一个字段，_ 来分割 
    */
	public static List queryByConditionList(HbaseQuery query, Object originObject,
			String tableTmp) throws Exception {
		

		List returnList = new ArrayList();
		String qaDevType =query.getType();
		if(StringUtils.isEmpty(qaDevType)){
			qaDevType ="1";
		}
		Configuration conf = CommonUtil.getConf(qaDevType);
        String tableName = tableTmp;
        HTable table = new HTable(conf, tableName);
        Scan s=new Scan();
        
        int querySize = query.getQuerySize();
        if(querySize!=0){
        	s.setFilter(new PageFilter(querySize));
        }
        String rowkey_prefix = query.getRowkeyPrefix();
        if(StringUtils.isNotEmpty(rowkey_prefix)){
        	s.setFilter(new PrefixFilter(Bytes.toBytes(rowkey_prefix)));
        }
        String start_row = query.getStartKey();
        if(StringUtils.isNotEmpty(start_row)){
        	s.setStartRow(Bytes.toBytes(start_row));
        }
        String stop_row = query.getStopKey();
        if(StringUtils.isNotEmpty(stop_row)){
        	s.setStartRow(Bytes.toBytes(stop_row));
        }
        List<HbaseColumnValueVo> columValueList = query.getColumValueList();
        if(columValueList!=null && columValueList.size()>0){
        	  FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        	  for(HbaseColumnValueVo vo :columValueList){
        		  Filter filter = new SingleColumnValueFilter(Bytes.toBytes(vo.getFamily()),Bytes.toBytes(vo.getColumn()), vo.getOp(),Bytes.toBytes(vo.getValue()));
        		  filterList.addFilter(filter);
        	  }
        	  s.setFilter(filterList);
        }
        
        ResultScanner ss=table.getScanner(s);
        Field[] childFields = originObject.getClass().getDeclaredFields();
        Field[] superFields = originObject.getClass().getSuperclass().getDeclaredFields();
        List<Field> fields = new ArrayList<Field>();
        for(Field field:childFields){
        	fields.add(field);
        }
        for(Field field:superFields){
        	fields.add(field);
        }
        
        for(Result rs:ss){
            originObject =originObject.getClass().newInstance();
            for (int i = 0; i < fields.size(); i++) {
            	Field field = fields.get(i);
            	field.setAccessible(true);
                String filedName = field.getName();
                //为了避免测试框架 自动注入字段
                if("$VRc".equals(filedName) ||"serialVersionUID".equals(filedName)){
                    continue;
                }
                Class<?> type = field.getType();
                String fieldSimpleTypeName = type.getSimpleName();
                Object fieldValue = null;
                byte[] fieldValueByte = CommonUtil.getFieldValue(tableName,filedName,rs);
                if ("Long".equals(fieldSimpleTypeName) && fieldValueByte != null) {
                    fieldValue = Bytes.toLong(fieldValueByte);
                } else if ("String".equals(fieldSimpleTypeName) && fieldValueByte != null) {
                    fieldValue = Bytes.toString(fieldValueByte);
                }else if (("Double".equals(fieldSimpleTypeName)|| "double".equals(fieldSimpleTypeName)) && fieldValueByte != null) {
                    fieldValue = Bytes.toDouble(fieldValueByte);
                }
                if(fieldValue==null){
                	continue;
                }
                CommonUtil.setter(originObject, filedName, fieldValue, type);
            }
            returnList.add(originObject);
        }



        return returnList;
    
	}
	
	
	
	
	/**
	    *condition  query 
	    rowkey 
	    */
		public static Object queryByConditionGet(HbaseQuery query, Object originObject,
 String tableTmp) throws Exception {

		String qaDevType = query.getType();
		if (StringUtils.isEmpty(qaDevType)) {
			qaDevType = "1";
		}
		if (StringUtils.isEmpty(query.getRowkey())) {
			return "-1";
		}
		Configuration conf = CommonUtil.getConf(qaDevType);
		String tableName = tableTmp;
		HTable table = new HTable(conf, tableName);
		Get get = new Get(Bytes.toBytes(query.getRowkey()));

		Result rs = table.get(get);
		Field[] childFields = originObject.getClass().getDeclaredFields();
		Field[] superFields = originObject.getClass().getSuperclass()
				.getDeclaredFields();
		List<Field> fields = new ArrayList<Field>();
		for (Field field : childFields) {
			fields.add(field);
		}
		for (Field field : superFields) {
			fields.add(field);
		}

		originObject = originObject.getClass().newInstance();
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			field.setAccessible(true);
			String filedName = field.getName();
			if (filedName.equals("huanshoulv")) {
				System.out.println(111);
			}
			// 为了避免测试框架 自动注入字段
			if ("$VRc".equals(filedName)
					|| "serialVersionUID".equals(filedName)) {
				continue;
			}
			Class<?> type = field.getType();
			String fieldSimpleTypeName = type.getSimpleName();
			Object fieldValue = null;
			byte[] fieldValueByte = CommonUtil.getFieldValue(tableName,
					filedName, rs);
			if ("Long".equals(fieldSimpleTypeName) && fieldValueByte != null) {
				fieldValue = Bytes.toLong(fieldValueByte);
			} else if ("String".equals(fieldSimpleTypeName)
					&& fieldValueByte != null) {
				fieldValue = Bytes.toString(fieldValueByte);
			} else if (("Double".equals(fieldSimpleTypeName) || "double"
					.equals(fieldSimpleTypeName)) && fieldValueByte != null) {
				fieldValue = Bytes.toDouble(fieldValueByte);
			}
			if (fieldValue == null) {
				continue;
			}
			CommonUtil.setter(originObject, filedName, fieldValue, type);
		}

		return originObject;

	}
	
	
	public static String hbaseInsertByObject(Object obj, String tableTmp,
			String familyTmp) throws Exception {

		String tableName = tableTmp;
		String tableFamily = familyTmp;

		Configuration conf = CommonUtil.getConf("1");

		if (obj == null) {
			return "-1";
		}
		String rowKey = CommonUtil.obj2string(CommonUtil.getter(obj, "rowkey"));
		if ("-1".equals(rowKey)) {
			return "-1";
		}
		HTable table = new HTable(conf, tableName);
		Put put = new Put(Bytes.toBytes(rowKey));
		Field[] childFields = obj.getClass().getDeclaredFields();
		Field[] superFields = obj.getClass().getSuperclass()
				.getDeclaredFields();
		List<Field> fields = new ArrayList<Field>();
		for (Field field : childFields) {
			fields.add(field);
		}
		for (Field field : superFields) {
			fields.add(field);
		}

		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			field.setAccessible(true);

			String filedName = field.getName();
			Class<?> type = field.getType();
			String fieldSimpleTypeName = type.getSimpleName();
			Object fieldValueTmp = CommonUtil.getter(obj, filedName);
			if ("Long".equals(fieldSimpleTypeName)) {
				Long fieldValue = CommonUtil.obj2long(fieldValueTmp);
				if (fieldValue != null) {
					put.add(Bytes.toBytes(tableFamily),
							Bytes.toBytes(filedName), Bytes.toBytes(fieldValue));
				}
			} else if ("String".equals(fieldSimpleTypeName)) {
				String fieldValue = CommonUtil.obj2string(fieldValueTmp);
				if (StringUtils.isNotEmpty(fieldValue)
						&& !fieldValue.equals("-1")) {
					put.add(Bytes.toBytes(tableFamily),
							Bytes.toBytes(filedName), Bytes.toBytes(fieldValue));
				}
			} else if ("Double".equals(fieldSimpleTypeName) || "double".equals(fieldSimpleTypeName)) {
				Double fieldValue = CommonUtil.obj2Double(fieldValueTmp);
				if (fieldValue != null && fieldValue != 0) {
					put.add(Bytes.toBytes(tableFamily),
							Bytes.toBytes(filedName), Bytes.toBytes(fieldValue));
				}
			}
		}
		table.put(put);
		table.flushCommits();
		table.close();
		return "1";
	}

	public static Put hbaseObj2put(Object obj, String tableTmp, String familyTmp)
			throws Exception {

		String tableName = tableTmp;
		String tableFamily = familyTmp;
		if (obj == null) {
			return null;
		}
		String rowKey = CommonUtil.obj2string(CommonUtil.getter(obj, "rowkey"));
		if ("-1".equals(rowKey)) {
			return null;
		}
		Put put = new Put(Bytes.toBytes(rowKey));
		Field[] childFields = obj.getClass().getDeclaredFields();
		Field[] superFields = obj.getClass().getSuperclass()
				.getDeclaredFields();
		List<Field> fields = new ArrayList<Field>();
		for (Field field : childFields) {
			fields.add(field);
		}
		for (Field field : superFields) {
			fields.add(field);
		}

		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			field.setAccessible(true);

			String filedName = field.getName();
			if(filedName.contains("serialVersionUID")){
				continue;
			}
			Class<?> type = field.getType();
			String fieldSimpleTypeName = type.getSimpleName();
			Object fieldValueTmp = CommonUtil.getter(obj, filedName);
			if ("Long".equals(fieldSimpleTypeName)) {
				Long fieldValue = CommonUtil.obj2long(fieldValueTmp);
				if (fieldValue != null) {
					put.add(Bytes.toBytes(tableFamily),
							Bytes.toBytes(filedName), Bytes.toBytes(fieldValue));
				}
			} else if ("String".equals(fieldSimpleTypeName)) {
				String fieldValue = CommonUtil.obj2string(fieldValueTmp);
				if (StringUtils.isNotEmpty(fieldValue)
						&& !fieldValue.equals("-1")) {
					put.add(Bytes.toBytes(tableFamily),
							Bytes.toBytes(filedName), Bytes.toBytes(fieldValue));
				}
			} else if ("Double".equals(fieldSimpleTypeName)) {
				Double fieldValue = CommonUtil.obj2Double(fieldValueTmp);
				if (fieldValue != null) {
					put.add(Bytes.toBytes(tableFamily),
							Bytes.toBytes(filedName), Bytes.toBytes(fieldValue));
				}
			}
		}
		return put;
	}

}
