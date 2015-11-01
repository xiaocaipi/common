package service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.security.UserGroupInformation;

import util.CommonUtil;


/**
 * Hello world!
 *
 */
public class App 
{
	private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	

    	public static void main(String[] args) throws Exception {
//    				String aa = CommonUtil.getOutPropertyValue("aaa");
//    				System.out.println(aa);
    		String stockTigerPath =CommonUtil.getPropertyValue("stockTigerPath");
    		File file = new File(stockTigerPath+"data1.txt");
    		File parentDirectory = new File(file.getParent());
    		
    		if(!parentDirectory.exists()&& !parentDirectory .isDirectory()){
    			parentDirectory.mkdir();
    		}
    		
		}
    	
    	public static Configuration getConf() throws IOException{
//    		return getQAConf();
    		return getDevConf();
    	}
    	
    	public static Configuration getDevConf() throws IOException{
    		Configuration config = HBaseConfiguration.create();
    		config.set("hbase.zookeeper.quorum", "hadoop1,hadoop2,hadoop3,hadoop4,hadoop5");
    		config.set("hbase.zookeeper.property.clientPort", "2181");

    		return config;
    	}
}
