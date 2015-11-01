package util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;

import com.stock.vo.ProxyVo;

import service.ProxyService;

public class CommonUtil {

	// public static String path="/data/app/tomcat/data/";
	//要废弃掉的字段，改成从配置文件中获取
	public static String path = "F:\\project\\stock\\data\\";
	public static String codeshpath = "F:\\project\\stock\\data\\SH.SNT";
	public static String codeszpath = "F:\\project\\stock\\data\\SZ.SNT";
	public static String outputpath = "F:\\project\\stock\\output\\";
	private static SimpleDateFormat fmtspecial = new SimpleDateFormat(
			"yyyyMMdd");
	private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat fmt1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static DecimalFormat df = new DecimalFormat("#.00");

	private static Configuration config = null;
	
	private static String qaDevType ="";

	public static String getDateFromString(String data) {
		String returndata = "";
		Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		Matcher matcher = pattern.matcher(data);
		while (matcher.find()) {
			returndata = matcher.group(0);
		}
		return returndata;
	}

	// 鍓嶉棴鍚庨棴
	public static List<String> getDatesByDay(String tmptimestart,
			String tmptimeend) {
		// String tmptimestart="2014-08-01";
		// String tmptimeend="2014-08-05";

		Date date = new Date();
		if (StringUtils.isEmpty(tmptimeend)) {
			tmptimeend = fmt.format(date);
		}
		List<String> returnlist = new ArrayList<String>();
		try {
			Calendar calendar = new GregorianCalendar();

			calendar.setTime(date);
			Date startdate = fmt.parse(tmptimestart);
			Date enddate = fmt.parse(tmptimeend);
			long num = CommonTool.caculateTimeBetween(startdate, enddate);
			long time1 = System.currentTimeMillis();

			for (int i = 0; i <= num; i++) {
				calendar.setTime(startdate);
				calendar.add(calendar.DATE, i);
				String date2 = fmt.format(calendar.getTime());
				returnlist.add(date2);
				// System.out.println(date2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return returnlist;
	}

	public static void createDZHdata(HashMap<String, Integer> tmpmap,
			List<String> tmplist, String filename) {
		Calendar calendar = new GregorianCalendar();
		Date tmpdata2 = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

		// tmpmap 鏀剧殑鏄鍚堢殑鏁版嵁锛屼絾鏄ぇ鏅烘収宸ュ叿閲岄潰瑕佹妸涓嶇鍚堢殑鏃ユ湡涔熻璁剧疆涓?
		for (String tmpcode2 : tmplist) {
			for (int i = 0; i > -1000; i--) {
				calendar.setTime(tmpdata2);
				calendar.add(calendar.DATE, i);
				String date2 = fmt.format(calendar.getTime());
				String codedate = tmpcode2 + "-" + date2;
				if (tmpmap.get(codedate) == null) {
					CommonTool.formatFlie(filename, tmpcode2 + "\t" + date2
							+ "\t" + "0" + "\n", false);
				} else {
					CommonTool.formatFlie(filename, tmpcode2 + "\t" + date2
							+ "\t" + "2" + "\n", false);
				}
			}
		}
	}

	public static String praseDateToString(Date date, boolean special) {
		String returnDate = "";
		if (special) {
			returnDate = fmtspecial.format(date);
		} else {
			returnDate = fmt.format(date);
		}
		return returnDate;
	}

	public static void fileCommonList(List<Map<String, Object>> list) {
		String printline = "";
		for (Map<String, Object> map : list) {
			String tmpprintline1 = "";
			for (String s : map.keySet()) {
				// String tmp1=(String)map.get(s);
				if (tmpprintline1.equals("")) {
					tmpprintline1 = "" + map.get(s);
				} else {
					tmpprintline1 = tmpprintline1 + "\t" + map.get(s);
				}

			}
			printline = printline + "\n" + tmpprintline1;
		}
		try {
			FileMyUtil.print(printline, "test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 鍙犲埌map鐨勯『搴?
	 * 
	 * @return
	 */
	public static Map<String, Object> converMap(Map<String, Object> paraMap) {
		LinkedHashMap<String, Object> returnMap = new LinkedHashMap<String, Object>();
		for (String s : paraMap.keySet()) {
			returnMap.put(s, paraMap.get(s));
		}
		return returnMap;
	}


	public static String obj2string(Object f) {
		String returnString = "";
		if (f instanceof Long) {
			long tmp1 = (Long) f;
			returnString = String.valueOf(tmp1);
		} else if (f instanceof Integer) {
			int tmp1 = (Integer) f;
			returnString = String.valueOf(tmp1);
		} else if (f instanceof BigDecimal) {
			BigDecimal tmp1 = (BigDecimal) f;
			returnString = tmp1.toString();
		} else if (f instanceof BigInteger) {
			BigInteger tmp1 = (BigInteger) f;
			returnString = tmp1.toString();
		} else {
			returnString = (String) f;
		}
		if (StringUtils.isEmpty(returnString)) {
			returnString = "-1";
		}
		return returnString;
	}

	public static String getRegexString(String str, String regex1) {
		String regex = regex1;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			sb.append(matcher.group());
		}
		return sb.toString();
	}

	public static void useProxy(Map<String, Object> paraMap) {
		ProxyService proxyService = (ProxyService) paraMap.get("proxyService");
		List<ProxyVo> proxyList = proxyService.getUsefulProxyList(paraMap);
		int listsize = proxyList.size();
		int random = CommonUtil.getRandomNum(0, listsize - 1);
		ProxyVo vo = proxyList.get(random);
		String ip = vo.getIp();
		String port = vo.getPort();
		System.setProperty("http.proxyHost", ip);
		System.setProperty("http.proxyPort", port);

	}

	public static int getRandomNum(int min, int max) {
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		return time;
	}

	public static boolean isInChangeTime() {
		boolean returnValue = false;
		String currentStringTime = CommonUtil.getCurrentTime();

		try {
			Date current = fmt1.parse(currentStringTime);
			Date beginTime1 = fmt1.parse(currentStringTime.split(" ")[0] + " "
					+ "09:15:00");
			Date endTime1 = fmt1.parse(currentStringTime.split(" ")[0] + " "
					+ "11:35:00");
			Date beginTime2 = fmt1.parse(currentStringTime.split(" ")[0] + " "
					+ "12:55:00");
			Date endTime2 = fmt1.parse(currentStringTime.split(" ")[0] + " "
					+ "15:05:00");
			if (current.getTime() >= beginTime1.getTime()
					&& current.getTime() <= endTime1.getTime()) {
				returnValue = true;
			} else if (current.getTime() >= beginTime2.getTime()
					&& current.getTime() <= endTime2.getTime()) {
				returnValue = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return returnValue;

	}
	
	public static Configuration getConf(String type) throws IOException {
		qaDevType = type;
		if(config == null){
			if(type.equals("1")){
				String zkQuorum = getPropertyValue("zkQuorum");
				config = HBaseConfiguration.create();
				config.set("hbase.zookeeper.quorum", zkQuorum);
				config.set("hbase.zookeeper.property.clientPort", "2181");
			}else{
				config = getKerberosConf(type);
			}
		}
		
			

		return config;
	}

	private static Configuration getKerberosConf(String type) throws IOException {

		
			String krbconfPath = getPropertyValue("krbconfPath");
			String principal = getPropertyValue("principal");
			String keytabPath = getPropertyValue("keytabPath");
			String hbaseSitePath = getPropertyValue("hbase.site.path");
			String hiveSitePath = getPropertyValue("hive.site.path");
			config = HBaseConfiguration.create();
			config.addResource(new Path(hbaseSitePath));
			config.addResource(new Path(hiveSitePath));

			System.setProperty("java.security.krb5.conf", krbconfPath);
			config.set("hadoop.security.authentication", "kerberos");
			UserGroupInformation.setConfiguration(config);
			UserGroupInformation.loginUserFromKeytab(principal, keytabPath);
			
			

		return config;
	}

	public static String getPropertyValue(String key) {
		if(StringUtils.isEmpty(qaDevType)){
			qaDevType ="1";
		}
		String currentSystem = System.getProperty("os.name");
		String propertiesName = "";
		if (currentSystem.toLowerCase().contains("linux")) {
			if(qaDevType.equals("1")){
				propertiesName = "linux_dev_env.properties";
			}else if(qaDevType.equals("2")){
				propertiesName = "linux_qa_env.properties";
			}
			
		} else {
			if(qaDevType.equals("1")){
				propertiesName = "win_dev_env.properties";
			}else if(qaDevType.equals("2")){
				propertiesName = "win_qa_env.properties";
			}
		}
		String propFileName = new File(propertiesName).getName();
		int index = propFileName.indexOf(".properties");
		if (index > 0) {
			propFileName = propFileName.substring(0, index);
		}
		ResourceBundle rsb = ResourceBundle.getBundle(propFileName, Locale.US);
		String url = rsb.getString(key);
		return url;
	}
	
	public static String getOutPropertyValue(String key) {
		if(StringUtils.isEmpty(qaDevType)){
			qaDevType ="1";
		}
		String currentSystem = System.getProperty("os.name");
		String propertiesName = "";
		if (currentSystem.toLowerCase().contains("linux")) {
			if(qaDevType.equals("1")){
				propertiesName = "linux_dev_out_env.properties";
			}else if(qaDevType.equals("2")){
				propertiesName = "linux_qa_out_env.properties";
			}
			
		} else {
			if(qaDevType.equals("1")){
				propertiesName = "F:\\project\\property\\win_dev_out_env.properties";
			}else if(qaDevType.equals("2")){
				propertiesName = "F:\\project\\property\\win_qa_out_env.properties";
			}
		}
		String propFileName = new File(propertiesName).getName();
		int index = propFileName.indexOf(".properties");
		if (index > 0) {
			propFileName = propFileName.substring(0, index);
		}
		ResourceBundle rsb = ResourceBundle.getBundle(propFileName, Locale.US);
		String url = rsb.getString(key);
		return url;
	}
	
	 public static void setter(Object obj, String att, Object value,
				Class<?> type) {
			try {
				Method method = obj.getClass().getMethod("set" + captureName(att),
						type);
				method.invoke(obj, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	    public static byte[] getFieldValue(String tableName,String field,Result rs){
	        if(field == null){
	            return null;
	        }
	        byte[] returnByte = null;
	        List<Cell> cells = rs.listCells();
	        for(Cell cell:cells){
	            String filedName = Bytes.toString(cell.getQualifier());
	            if(field.equals(filedName)){
	                returnByte = cell.getValue();
	            }
	        }
	        return returnByte;
	    }
	    
	    public static String captureName(String name) {
	        name = name.substring(0, 1).toUpperCase() + name.substring(1);
	        return  name;

	    }
	    
	    public static Object getter(Object obj, String att) throws Exception {
	        Method method = obj.getClass().getMethod("get" + captureName(att));
	        return method.invoke(obj);
	    }
	    
	    public static String  getDatesByDay(String time,
    			int  n) {

    		try {
    			Calendar calendar = new GregorianCalendar();

    			Date startdate = fmt.parse(time);
    			calendar.setTime(startdate);
    			calendar.add(calendar.DATE, n);
				String date2 = fmt.format(calendar.getTime());
				return date2;
    		} catch (Exception e) {
    			e.printStackTrace();
    			throw new RuntimeException(e);
    		}
    		
    	}

		
    public static Long obj2long(Object f) {
        if (f instanceof Long) {
            long tmp1 = (Long) f;
            return tmp1;
        }else{
            return null;
        }
    }
    
    
    public static Double obj2Double(Object f) {
        if (f instanceof Double) {
            double tmp1 = (Double) f;
            return tmp1;
        }else{
            return null;
        }
    }
    
    public static String getCurrentDate(){
    	return fmt.format(new Date());
    }
    
    public static String toString(Object obj) throws Exception {

        String returnString ="";
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
            if("$VRc".equals(filedName) ||"serialVersionUID".equals(filedName)){
                continue;
            }
            Class<?> type = field.getType();
            String fieldSimpleTypeName = type.getSimpleName();
            Object fieldValueTmp = getter(obj, filedName);
            if ("Long".equals(fieldSimpleTypeName) && fieldValueTmp != null) {
                Long fieldValue = CommonUtil.obj2long(fieldValueTmp);
                returnString = returnString+"\t"+"字段:"+filedName+" 值是:"+fieldValue;
            } else if ("String".equals(fieldSimpleTypeName) && fieldValueTmp != null) {
                String fieldValue = CommonUtil.obj2string(fieldValueTmp);
                returnString = returnString+"\t"+"字段:"+filedName+" 值是:"+fieldValue;
            }else if (("Double".equals(fieldSimpleTypeName) || "double"
					.equals(fieldSimpleTypeName)) && fieldValueTmp != null) {
            	Double fieldValue = CommonUtil.obj2Double(fieldValueTmp);
            	 returnString = returnString+"\t"+"字段:"+filedName+" 值是:"+fieldValue;
				
			}
        }
        return  returnString;
    }
    
    public static int getRandonNum(int min,int max){
    	int random=(int)(Math.random()*(max+1-min)+min) ;
    	return random;
    }
	    
	    

}
