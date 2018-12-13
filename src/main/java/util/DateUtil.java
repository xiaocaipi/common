package util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

	public static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static String getDateBefore(Object d, int day) {
		Calendar now = Calendar.getInstance();
		try {
			if(d.getClass().getName().equals("java.util.Date")){
				Date time=(Date)d;
				now.setTime(time);
			}else if(d.getClass().getName().equals("java.lang.String")){
				String time=String.valueOf(d);
				now.setTime(fmt.parse(time));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return fmt.format(now.getTime());
	}

	public static String getTradeDayByN(String baseDate, int n, List<String> holidayList) throws IOException, ParseException {
//		String filePath = CommonUtil.getPropertyValue("holidayPath");
//		String content  = FileMyUtil.readFile(filePath);
//		String[] holidays=content.split("\n");
		Map<String,Integer> holidayMap  = new HashMap<String,Integer>();
		for(String holiday :holidayList){
			holidayMap.put(holiday,1);
		}

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=1;i<=n;i++){
			String tmpDate = getDateBefore(baseDate,i);
			//是一个holiday
			if(holidayMap.containsKey(tmpDate)){
				n++;
			}
		}

		String returnDate = getDateBefore(baseDate,n);
		return returnDate;
	}

	public static void main(String[] args) throws IOException, ParseException {
		List<String> list = new ArrayList<String>();
		list.add("2017-11-20");
		list.add("2017-11-19");
		System.out.println(getTradeDayByN("2017-11-21",1,list));
	}
}
