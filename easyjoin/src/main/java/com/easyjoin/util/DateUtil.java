/**
 * 
 */
package com.easyjoin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Smile.Li
 * @since 2018年11月5日
 */
public class DateUtil {
	/**
	 * 将字符串格式化成日期格式，并返回日期类型
	 * @param value 日期字符串
	 * @param inc 月份增量，正负均可
	 * @param expstr 格式化字符串，如"yyyyMM"
	 * @return 
	 */
	public static String addMonths(String value, int inc, String expStr){
		String val = value.replaceAll("(\\d{4})(\\d{2})(\\d{2})", "$1-$2-$3");
		try {
			Calendar c = Calendar.getInstance();
			DateFormat df = DateFormat.getDateInstance();
			c.setTime(df.parse(val));
			c.add(Calendar.MONTH, inc);
			DateFormat sdf = new SimpleDateFormat(expStr);
			val = sdf.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return val;
	}
	
	/**
	 * 将字符串格式化成日期格式，并返回日期类型
	 * @param value 日期字符串
	 * @param inc 月份增量，正负均可
	 * @param expstr 格式化字符串，如"yyyyMM"
	 * @return 
	 */
	public static String addMonths(Date date, int inc, String expStr){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, inc);
		DateFormat sdf = new SimpleDateFormat(expStr);
		String val = sdf.format(c.getTime());
		return val;
	}
	
	public static String addDays(Date date, int inc, String expStr){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, inc);
		DateFormat sdf = new SimpleDateFormat(expStr);
		String val = sdf.format(c.getTime());
		return val;
	}
	public static String formatDate(Date date){
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String formatDate(Date date, String expStr){
		DateFormat sdf = new SimpleDateFormat(expStr);
		return sdf.format(date);
	}
	
	/**
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date format(String dateStr){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		if(dateStr.length()<=10){
			dateStr += " 00:00:00"; 
		}
		try {
			date = df.parse(dateStr.replaceAll("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1-$2-$3 $4:$5:$6"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取两个时间之间的间隔长度
	 * @param cycleType 类型，Y:年间隔, M:月间隔, D:日间隔
	 * @param start
	 * @param end
	 * @return
	 * @see Nov 25, 2014
	 * @author think
	 */
	public static long getDateBetween(String cycleType, Date start, Date end){
		Calendar beginCalendar = Calendar.getInstance();  
        Calendar endCalendar = Calendar.getInstance();  
        beginCalendar.setTime(start);  
        endCalendar.setTime(end);
        int year = endCalendar.get(Calendar.YEAR)-beginCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)-beginCalendar.get(Calendar.MONTH);
        long day = end.getTime() - start.getTime();
        if("Y".equals(cycleType.toUpperCase())){
        	return year;
        }else if("M".equals(cycleType.toUpperCase())){
        	return 12*year+month;
        }else if("D".equals(cycleType.toUpperCase())){
        	return day/(24*60*60*1000);
        }else{
        	return 0;
        }
	}
	
	/**
	 * 获取账期列表
	 * @param cycleType 类型，Y:年间隔, M:月间隔, D:日间隔
	 * @param start 对应cycleType的起始日期，Y对应年如2014，M对应年月如201401，D对应年月日如20140101
	 * @param hasCurrCycle 是否包含当前账期 true包含,false不包含
	 * @return
	 * @see Nov 25, 2014
	 * @author think
	 */
	public static List<Map<String,String>> getMonthCycle(String cycleType, int start, boolean hasCurrCycle){
		List<Map<String,String>> cycleList = new ArrayList<Map<String,String>>();
		String startStr = "", formatStr="";
		int baseInc = 1;
		if("Y".equals(cycleType.toUpperCase())){
			startStr = start+"0101";
			formatStr="yyyy";
			baseInc = 12;
        }else if("M".equals(cycleType.toUpperCase())){
        	startStr = start+"01";
        	formatStr="yyyyMM";
        }else if("D".equals(cycleType.toUpperCase())){
        	startStr = start+"";
        	formatStr="yyyyMMdd";
        }
		Date startDate = DateUtil.format(startStr.replaceAll("(\\d{4})(\\d{2})(\\d{2})", "$1-$2-$3"));
		Date endDate = new Date();
		long between = getDateBetween(cycleType, startDate, endDate);
		int initStart = 1;
		if(hasCurrCycle){
			initStart = 0;
		}
		for(int i = initStart ; i<=between; i++){
			String cycleId = "";
			if("D".equals(cycleType.toUpperCase())){
				cycleId = addDays(endDate, -baseInc*i, formatStr); 
			}else{
				cycleId = addMonths(endDate, -baseInc*i, formatStr);
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("cycleId", cycleId);
			cycleList.add(map);
		}
		return cycleList;
	}
	
	public static void main(String[] args){
		/*java.util.Calendar c = java.util.Calendar.getInstance();
		java.text.DateFormat df = java.text.DateFormat.getDateInstance();
		c.setTime(new java.util.Date());
		int nyear = c.get(java.util.Calendar.YEAR);
		int nmon = c.get(java.util.Calendar.MONTH);
		int nday = c.get(java.util.Calendar.DATE);
		try {
			c.setTime(df.parse("2010-03-10"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int year = c.get(java.util.Calendar.YEAR);
		int mon = c.get(java.util.Calendar.MONTH);
		if(nyear == year && nmon == mon && nday < 10){
			c.add(java.util.Calendar.MONTH, -2);
		}else{
			c.add(java.util.Calendar.MONTH, -1);
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMM");
		System.out.println(sdf.format(c.getTime()));*/
		//System.out.print(DateUtil.addMonths("20100310", -1, "yyyyMM"));
		//System.out.print("2010-03".replaceAll("(\\d{4})-(\\d{2})-(\\d{2})", "$1年$2月"));
		
		System.out.print(DateUtil.getMonthCycle("D", 20141101, true));
	}
}
