package util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.stock.vo.ProxyVo;

import service.ProxyService;



public class CommonUtil {
	
	
//	public static String path="/data/app/tomcat/data/";
	public static String path="F:\\project\\stock\\data\\";
	public static String codeshpath="F:\\project\\stock\\data\\SH.SNT";
	public static String codeszpath="F:\\project\\stock\\data\\SZ.SNT";
	public static String outputpath="F:\\project\\stock\\output\\";
	private static SimpleDateFormat fmtspecial=new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
	private static DecimalFormat df = new DecimalFormat("#.00");
	
	
	
	
	public static String getDateFromString (String data){
		String returndata="";
		Pattern pattern=Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		Matcher matcher = pattern.matcher(data);
		while(matcher.find()){
			returndata=matcher.group(0);
		}
		return returndata;
	}
	//鍓嶉棴鍚庨棴
	public static List<String> getDatesByDay(String tmptimestart,String tmptimeend){
//		String tmptimestart="2014-08-01";
//		String tmptimeend="2014-08-05";
		
		Date date=new Date();
		if(StringUtils.isEmpty(tmptimeend)){
			tmptimeend=fmt.format(date);
		}
		List<String> returnlist=new ArrayList<String>();
		try {
			Calendar calendar = new GregorianCalendar();
			
			 
			 calendar.setTime(date);
			 Date startdate=fmt.parse(tmptimestart);
			 Date enddate=fmt.parse(tmptimeend);
			 long num=CommonTool.caculateTimeBetween(startdate, enddate);
			 long time1=System.currentTimeMillis();
			
			
			for(int i=0;i<=num;i++){
				calendar.setTime(startdate);
				calendar.add(calendar.DATE, i);
				String date2=fmt.format(calendar.getTime());
				returnlist.add(date2);
//				System.out.println(date2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return returnlist;
	}

	
	
	
	public static void createDZHdata(HashMap<String, Integer> tmpmap,
			List<String> tmplist,String filename) {
		Calendar calendar = new GregorianCalendar();
		Date tmpdata2=new Date();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
		
		//tmpmap 鏀剧殑鏄鍚堢殑鏁版嵁锛屼絾鏄ぇ鏅烘収宸ュ叿閲岄潰瑕佹妸涓嶇鍚堢殑鏃ユ湡涔熻璁剧疆涓? 
		for(String tmpcode2:tmplist){
			for(int i=0;i>-1000;i--){
				calendar.setTime(tmpdata2);
				calendar.add(calendar.DATE, i); 
				String date2=fmt.format(calendar.getTime());
				String codedate=tmpcode2+"-"+date2;
				if(tmpmap.get(codedate)==null){
					CommonTool.formatFlie(filename, tmpcode2 +"\t"+date2+"\t"+"0"+"\n",false);
				}else{
					CommonTool.formatFlie(filename, tmpcode2 +"\t"+date2+"\t"+"2"+"\n",false);
				}
			}
		}
	}
	
	public static String praseDateToString(Date date,boolean special){
		String returnDate="";
		if(special){
			returnDate=fmtspecial.format(date);
		}else{
			returnDate=fmt.format(date);
		}
		return returnDate;
	}
	
	
	
	
	
	
	
	
	public static void fileCommonList (List<Map<String, Object>> list) {
		String printline="";
		for(Map<String, Object> map:list){
			String tmpprintline1="";
			for(String s:map.keySet()){
//				String tmp1=(String)map.get(s);
				if(tmpprintline1.equals("")){
					tmpprintline1 =""+map.get(s);
				}else{
					tmpprintline1 =tmpprintline1+"\t"+map.get(s);
				}
				
			}
			printline = printline+"\n"+tmpprintline1;
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
	public static Map<String, Object>  converMap(Map<String, Object> paraMap) {
		LinkedHashMap<String, Object> returnMap =new LinkedHashMap<String, Object>();
		for(String s:paraMap.keySet()){
			returnMap.put(s, paraMap.get(s));
		}
		return returnMap;
	}
	
	public static void main(String[] args) {
		
//		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
//		LinkedHashMap<String, Object> mapName = new LinkedHashMap<String, Object>();
//		Map<String, Object> map2 = new HashMap<String, Object>();
//		Map<String, Object> map3 = new HashMap<String, Object>();
//		mapName.put("todayzhangfu","todayzhangfu");
//		mapName.put("ref1zhangfu","ref1zhangfu");
//		mapName.put("todaySHzhangfu","todaySHzhangfu");
//		mapName.put("ref1SHzhangfu","ref1SHzhangfu");
//		mapName.put("todayCYBzhangfu","todayCYBzhangfu");
//		mapName.put("ref1CYBzhangfu","ref1CYBzhangfu");
//		converMap(mapName);
//		list.add(mapName);
//		list.add(map2);
//		list.add(map3);
		
//		fileCommonList(list);
		
		
		String aa = getRegexString("welcome to china,江西奉新,(3123)welcome,你1231!","\\([0-9]*\\)");  
		System.out.println(aa);
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
	
	public static String getRegexString(String str,String regex1){  
        String regex = regex1;
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            sb.append(matcher.group());  
        }  
        return sb.toString();
    }
	public static void useProxy(Map<String, Object> paraMap) {
		ProxyService proxyService = (ProxyService)paraMap.get("proxyService");
		List<ProxyVo> proxyList =proxyService.getUsefulProxyList(paraMap);
		int listsize = proxyList.size();
		int random = CommonUtil.getRandomNum(0, listsize-1);
		ProxyVo vo = proxyList.get(random);
		String ip=vo.getIp();
		String port=vo.getPort();
		System.setProperty("http.proxyHost",ip);
		System.setProperty("http.proxyPort", port);
		
	} 
	
	public static int getRandomNum(int min,int max){
		 Random random = new Random();

	     int s = random.nextInt(max)%(max-min+1) + min;
	     return s;
	}
	
	

}
