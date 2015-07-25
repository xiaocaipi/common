package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.stock.vo.ProxyVo;

public class FetchUtil {
	
	public static void main(String[] args) throws Exception {
	    Map<Integer, String> map = new HashMap<Integer, String>();  
		for (int i = 0; i < 2000; i++) {  
            map.put(i, i * 10 + "");  
        }  
		
		for (Map.Entry<Integer, String> entry : map.entrySet()) {  
            System.out.println("key = " + entry.getKey() + " and value = " + entry.getValue());  
        }  
	}
	
	public static List<Object> commonFetch(HashMap<String, Object> map) throws Exception {
		
		List<Object> returnList = new ArrayList<Object>();
		//需要1个vo  返回的vo   
		//需要 规则爬去的规则
		Object vo = map.get("vo");
		Field [] voFields = vo.getClass().getDeclaredFields();
		String url = (String)map.get("url");
		Document doc = null;
		doc = NetUtil.goFetch(url, doc, map);
		Map<String, String> fetchRuleMap = (Map<String, String>)map.get("fetchRuleMap");
		Map<String, String> positionMap = (Map<String, String>)map.get("positionMap");
		Map<String, String> regexMap = (Map<String, String>)map.get("regexMap");
		Map<String, Elements> tmpmap1 = new HashMap<String, Elements>();//map用于存放临时数据
		for(Field voField :voFields){
			voField.setAccessible(true);
			String fieldName = voField.getName();
			String rule = fetchRuleMap.get(fieldName);
			String position = positionMap.get(fieldName);
			if(StringUtils.isNotEmpty(rule)){
				Elements elements = doc.select(rule);
//				for(Element element : elements){
//					//查找位置 比如a标签的text
//					if(StringUtils.isEmpty(position) || position.equals("text")){
//						String text = element.text();
//						
//					}
//				}
				tmpmap1.put(fieldName, elements);
			}
		}
		
		for (Map.Entry<String, Elements> entry : tmpmap1.entrySet()) {  
//            System.out.println("key = " + entry.getKey() + " and value = " + entry.getValue());  
			
			
        } 
		
		
		return returnList;
	}

}
