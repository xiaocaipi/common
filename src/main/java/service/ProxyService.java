package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stock.vo.ProxyVo;

//主要去获取一些代理ip的service
public interface ProxyService {

	void crawlerproxyinxici(HashMap<String, Object> hashMap);

	void testProxy(HashMap<String, Object> hashMap);

	List<ProxyVo> getUsefulProxyList(Map<String, Object> map);

}
