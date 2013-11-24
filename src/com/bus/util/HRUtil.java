package com.bus.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HRUtil {

	/**
	 * 获取MD5字符串，将参数转换成MD5，账号密码专用
	 * @param str
	 * @return
	 */
	public static String getStringMD5(String str) {
		try {
			str = str + "shundeqiche";
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(Integer.toHexString((int) (b & 0xff)));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取一个字节的第position位数
	 * @param b
	 * @param position
	 * @return
	 */
	public static int getBit(byte b, int position) {
		return (int) ((b >> position) & 1);
	}

	/**
	 * 设置一个字节的position位数到1
	 * @param b
	 * @param position
	 * @return
	 */
	public static byte setBit(byte b, int position) {
		return (byte) (b | (1 << position));
	}

	/**
	 * 转换字符串日期到日期类型
	 * @param str
	 * @param strFormat
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(String str, String strFormat) throws Exception{
		try {
			if(str.equals(""))
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
			Date date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			String[] formats = new String[]{"yyyy/MM/dd","yyyy-MM-dd","yyyy-MM-dd","yyyy.MM.dd",
					"yyyy年MM月","yyyy年MM月dd日","yyyy.MM-dd","yyyy-MM.dd","yyyy/MM","yyyy-MM"};
			for(int i=0; i<formats.length;i++){
//				System.out.println("TRYING: "+formats[i]);
				try {
					SimpleDateFormat sdf = new SimpleDateFormat(formats[i]);
					Date date = sdf.parse(str);
					return date;
				} catch (ParseException e2) {
//					System.out.println(e2.getMessage());
				}
			}
		}
		throw new Exception("["+str + "] 日期格式不正确");
	}
	
	/**
	 * 转换日期类型到字符串类型yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String parseDateToString(Date date){
		if(date == null)
			return "";
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());
	}
	
	/**
	 * 设置浮点数格式
	 * @param num
	 * @return
	 */
	public static String formatFloatNumberComma(String num){
		Float amount = Float.parseFloat(num);
		NumberFormat formatter = NumberFormat.getInstance();
		return formatter.format(amount);
	}
	
	/**
	 * 转换url参数到Map
	 * @param queryString
	 * @return
	 */
	public static Map<String,String> parseRequestToMap(String queryString){
		String[] split1 = queryString.split("&");
		Map<String,String> map = new HashMap<String,String>();
		for(int i=0;i<split1.length;i++){
			String[] split2 = split1[i].split("=");
			if(split2.length>1)
				map.put(split2[0], split2[1]);
			else
				map.put(split2[0], null);
		}
		return map;
	}
	
	/**
	 * 获取文件的后缀名字
	 * @param filename
	 * @return
	 */
	public static String getFileExtension(String filename){
		return filename.substring(filename.lastIndexOf("."),filename.length());
	}
	
	/**
	 * 移除非数字的字符
	 * @param value
	 * @return
	 */
	public static String removeNoneNumber(String value) {
		char c[] = value.toCharArray();
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<c.length;i++){
			if(Character.isDigit(c[i])){
				builder.append(c[i]);
			}
		}
		return builder.toString();
	}

	/**
	 * 检查日期是否在本周范围
	 * @param date
	 * @return
	 */
	public static boolean isDateInCurrentWeek(Date date) {
		  Calendar currentCalendar = Calendar.getInstance();
		  int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
		  int year = currentCalendar.get(Calendar.YEAR);
		  Calendar targetCalendar = Calendar.getInstance();
		  targetCalendar.setTime(date);
		  int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
		  int targetYear = targetCalendar.get(Calendar.YEAR);
		  return week == targetWeek && year == targetYear;
		}
	
	/**
	 * 排序一个Map对象。对他们的值进行排序，不是key
	 * @param unsortMap
	 * @return
	 */
	public static Map sortByComparator(Map unsortMap) {
		 
		List list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
                                       .compareTo(((Map.Entry) (o1)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	 /**
	   * Convert an exception to a String with full stack trace
	   * @param ex the exception
	   * @return a String with the full stacktrace error text
	   */
	  public static String getStringFromStackTrace(Throwable ex)
	  {
	      if (ex==null)
	      {
	          return "";
	      }
	      StringWriter str = new StringWriter();
	      PrintWriter writer = new PrintWriter(str);
	      try
	      {
	          ex.printStackTrace(writer);
	          return str.getBuffer().toString();
	      }
	      finally
	      {
	          try
	          {
	              str.close();
	              writer.close();
	          }
	          catch (IOException e)
	          {
	              //ignore
	          }
	      }
	  }
	  
	  /**
	   * 检查字符串是否为整数
	   * @param s
	   * @return
	   */
	  public static boolean isInteger(String s) {
		    try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    // only got here if we didn't return false
		    return true;
		}
}
