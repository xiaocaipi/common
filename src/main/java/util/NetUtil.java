package util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import service.ProxyService;

import com.stock.vo.ProxyVo;


public class NetUtil {

	
	public static Document goFetch(String districtUrl, Document doc,HashMap<String, Object> paraMap) throws Exception {
		ProxyVo proxyvo=(ProxyVo)paraMap.get("proxy");
		String timeout=(String)paraMap.get("timeout");
		String ip ="";
		if(timeout==null){
			timeout="2000";
		}
		
		if(proxyvo!=null){
			ip=proxyvo.getIp();
			String port=proxyvo.getPort();
			System.setProperty("http.proxySet", "true"); 
			System.setProperty("http.proxyHost",ip);
			System.setProperty("http.proxyPort", port);
		}
		
		try {
			doc = Jsoup.connect(districtUrl).timeout(Integer.parseInt(timeout)).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36").get() ;
		} catch (IOException e) {
			System.out.println("获取网页--"+districtUrl+"失败"+"失败的ip---"+ ip);
		}
		Thread.sleep(1000);
		return doc;
	}
	
	//paraMap  isProxy   是否开启代理  1 是开启  空 或者2 是开启   默认不开启
	//paraMap  需要开启代理要传入proxyService
	public static Document goFetchIsProxy(String districtUrl, Document doc,HashMap<String, Object> paraMap) throws Exception {
		
		String isProxy = CommonUtil.obj2string(paraMap.get("isProxy"));
//		要开启代理的话
		if(!isProxy.equals("-1")){
			ProxyService proxyService = (ProxyService)paraMap.get("proxyService");
			List<ProxyVo> list = proxyService.getUsefulProxyList(new HashMap<String, Object>());
			int random = CommonUtil.getRandomNum(0, list.size());
			ProxyVo vo = list.get(random);
			paraMap.put("proxy", vo);

		}
		
		return goFetch(districtUrl, doc, paraMap);
	}
}
