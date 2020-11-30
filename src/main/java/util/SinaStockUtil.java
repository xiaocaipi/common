package util;


import com.stock.vo.StockRealTimeData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SinaStockUtil {
	
	public static void main(String args[]) throws Exception {
			 
		   

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("code", "sz002565,sz000723,sz000713,sh603032,sz002057,sh000001,sz399006");
		alertprice(paraMap);


	}
	


	public static void checkCode(Map<String, Object> paraMap) throws InterruptedException{
		
		 String paracode =CommonUtil.obj2string(paraMap.get("code"));
		 List<StockRealTimeData> list1=getRealTimeDataHasLast(paracode,null,paraMap);
		 Thread.sleep(1000l);
	}
	
	
	
	public static void alertprice(Map<String, Object> paraMap) throws InterruptedException{
		 String paracode =CommonUtil.obj2string(paraMap.get("code"));
		 List<StockRealTimeData> list1=getRealTimeDataHasLast(paracode,null,paraMap);
		 Map<String, StockRealTimeData> refMap =new HashMap<String, StockRealTimeData>();
		 for(StockRealTimeData realTimeData :list1){
			 String code=realTimeData.getCode();
			 refMap.put(code, realTimeData);
		 }
		 boolean issend =false;
		 double alertprice=44.6;
		 while (true){
			 List<StockRealTimeData> list=getRealTimeDataHasLast(paracode,refMap,null);
			 for(StockRealTimeData realTimeData :list){
				 String code=realTimeData.getCode();
				 refMap.put(code, realTimeData);
				 if(code.equals("002727")){
					 if(realTimeData.getClose()<alertprice){
						 //如果发�?过了就不用发送了
						 if(!issend){
//							 MyAuthenticator aa=new MyAuthenticator();
//						     aa.sendmessage(code +"达到�?+alertprice); 
//						     issend=true;
						 }
					 }
				 }
				 showStockRealTimeData(realTimeData);
			 }
			 System.out.println();
			 Thread.sleep(3000l);
		 }
	}
	private static DecimalFormat df = new DecimalFormat("#.00");


	public static void showStockRealTimeData(StockRealTimeData realTimeData) {

		if (realTimeData.getCode().equals("000001")
				|| realTimeData.getCode().equals("399006")) {
			System.out.print(realTimeData.getCode() + "--"
					+ realTimeData.getClose() + "--:"
					+ df.format(realTimeData.getZhangdie()) + "   ");
		} else {
			double buy3sum = realTimeData.getBuy1shou()+realTimeData.getBuy2shou()+realTimeData.getBuy3shou();
			double sell3sum = realTimeData.getSell1shou()+realTimeData.getSell2shou()+realTimeData.getSell3shou();

			System.out.print(
					""
//					+realTimeData.getCode() + "--"
							+ realTimeData.getClose() + "--:"
							+ df.format(buy3sum/sell3sum) + "--:"
							+ df.format(realTimeData.getZhangdiefudu()) + "--"
							+ realTimeData.getDealnowShou() / 100 + "   ");

		}

	}


	//convert all the stock code  to the list of 800 codes in a group
	public static List<String> convertBigCode2SmallCode(String codes){
		String [] code_array = codes.split(",");
		List<String> code_list = new ArrayList<String>();
		String codeTmp ="";
		for(int i = 0;i<code_array.length;i++){
			if(i%400==0){
				if(i!=0){
					code_list.add(codeTmp);
				}
				codeTmp = code_array[i];
			}else{
				codeTmp = codeTmp+","+code_array[i];
			}
			if(i==code_array.length-1){
				code_list.add(codeTmp);
			}
		}
		return code_list;
	}
	

	public static List<StockRealTimeData> getRealTimeDataHasLast(String  codes,Map<String, StockRealTimeData> refMap,Map<String, Object> paraMap){

		if (paraMap !=null){
			String isProxy = CommonUtil.obj2string(paraMap.get("isProxy"));
			if("1".equals(isProxy)){
				CommonUtil.useProxy(paraMap);
			}
		}

		URL ur = null;
		BufferedReader reader = null;
		List<StockRealTimeData>  returnlist= new ArrayList<StockRealTimeData>();
		List<String> code_list = convertBigCode2SmallCode(codes);
		for(String codesTmp : code_list){
			try {
				ur = new URL("http://hq.sinajs.cn/list="+codesTmp);
//				System.out.println(ur.toString());
				HttpURLConnection uc = (HttpURLConnection) ur.openConnection();
				reader = new BufferedReader(new InputStreamReader(ur.openStream(),
						"GBK"));
				String line;
				while ((line = reader.readLine()) != null ) {
					if(line.length()>100){
						StockRealTimeData stockdatavo = extract2StockRT(refMap, line);
						returnlist.add(stockdatavo);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return returnlist;

	}

	private static StockRealTimeData extract2StockRT(
			Map<String, StockRealTimeData> refMap, String line) {
		StockRealTimeData stockdatavo=new StockRealTimeData();
		String[] tmp1=line.split(",");
		for(int i=0;i<tmp1.length;i++){
			String tmp2=tmp1[i];
			if(i==0){
				String tmp3=tmp2.replaceAll("[(A-Za-z)]", "").replaceAll("_", "").replaceAll(" ", "");
				stockdatavo.setName(tmp3.split("=\"")[1]);
				stockdatavo.setCode(tmp3.split("=\"")[0]);
			}else if(i==1){
				stockdatavo.setOpen(Double.parseDouble(tmp2));
			}else if(i==2){
				stockdatavo.setRefclose(Double.parseDouble(tmp2));
			}else if(i==3){
				stockdatavo.setClose(Double.parseDouble(tmp2));
			}else if(i==4){
				stockdatavo.setHigh(Double.parseDouble(tmp2));
			}else if(i==5){
				stockdatavo.setLow(Double.parseDouble(tmp2));
			}else if(i==6){
				stockdatavo.setBuy1price(Double.parseDouble(tmp2));
			}else if(i==7){
				stockdatavo.setSell1price(Double.parseDouble(tmp2));
			}else if(i==8){
				stockdatavo.setDealSumShou(Double.parseDouble(tmp2));
			}else if(i==9){
				stockdatavo.setDealmoney(Double.parseDouble(tmp2));
			}else if(i==10){
				stockdatavo.setBuy1shou(Double.parseDouble(tmp2));
			}else if(i==11){
				stockdatavo.setBuy1price(Double.parseDouble(tmp2));
			}else if(i==12){
				stockdatavo.setBuy2shou(Double.parseDouble(tmp2));
			}else if(i==13){
				stockdatavo.setBuy2price(Double.parseDouble(tmp2));
			}else if(i==14){
				stockdatavo.setBuy3shou(Double.parseDouble(tmp2));
			}else if(i==15){
				stockdatavo.setBuy3price(Double.parseDouble(tmp2));
			}else if(i==16){
				stockdatavo.setBuy4shou(Double.parseDouble(tmp2));
			}else if(i==17){
				stockdatavo.setBuy4price(Double.parseDouble(tmp2));
			}else if(i==18){
				stockdatavo.setBuy5shou(Double.parseDouble(tmp2));
			}else if(i==19){
				stockdatavo.setBuy5price(Double.parseDouble(tmp2));
			}else if(i==20){
				stockdatavo.setSell1shou(Double.parseDouble(tmp2));
			}else if(i==21){
				stockdatavo.setSell1price(Double.parseDouble(tmp2));
			}else if(i==22){
				stockdatavo.setSell2shou(Double.parseDouble(tmp2));
			}else if(i==23){
				stockdatavo.setSell2price(Double.parseDouble(tmp2));
			}else if(i==24){
				stockdatavo.setSell3shou(Double.parseDouble(tmp2));
			}else if(i==25){
				stockdatavo.setSell3price(Double.parseDouble(tmp2));
			}else if(i==26){
				stockdatavo.setSell4shou(Double.parseDouble(tmp2));
			}else if(i==27){
				stockdatavo.setSell4price(Double.parseDouble(tmp2));
			}else if(i==28){
				stockdatavo.setSell5shou(Double.parseDouble(tmp2));
			}else if(i==29){
				stockdatavo.setSell5price(Double.parseDouble(tmp2));
			}
			else if(i==31){
				stockdatavo.setTime(tmp2);
			}
		}
		stockdatavo.setZhangdie(stockdatavo.getClose()-stockdatavo.getRefclose());
		stockdatavo.setZhangdiefudu((stockdatavo.getClose()-stockdatavo.getRefclose())/stockdatavo.getRefclose()*100);
		if(refMap !=null &&refMap.get(stockdatavo.getCode())!=null){
			StockRealTimeData refdata=refMap.get(stockdatavo.getCode());
			if(refdata.getDealSumShou()==0){
				stockdatavo.setDealnowShou(0.0);
			}else{
				stockdatavo.setDealnowShou(stockdatavo.getDealSumShou()-refdata.getDealSumShou());
			}
			
		}
		return stockdatavo;
	}
}
