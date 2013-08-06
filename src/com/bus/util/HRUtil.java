package com.bus.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HRUtil {

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

	public static int getBit(byte b, int position) {
		return (int) ((b >> position) & 1);
	}

	public static byte setBit(byte b, int position) {
		return (byte) (b | (1 << position));
	}

	public static Date parseDate(String str, String strFormat) throws Exception{
		try {
			if(str.equals(""))
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
			Date date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			String[] formats = new String[]{"yyyy/MM/dd","yyyy-MM-dd","yyyy.MM.dd","yyyy年MM月","yyyy年MM月dd日","yyyy.MM-dd","yyyy-MM.dd"};
			for(int i=0; i<formats.length;i++){
				try {
					SimpleDateFormat sdf = new SimpleDateFormat(formats[i]);
					Date date = sdf.parse(str);
					return date;
				} catch (ParseException e2) {
				}
			}
		}
		throw new Exception("["+str + "] 日期格式不正确");
	}
	
	public static String parseDateToString(Date date){
		if(date == null)
			return "";
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());
	}
	
	public static String formatNumberComma(String num){
		Long amount = Long.parseLong(num);
		NumberFormat formatter = NumberFormat.getInstance();
		return formatter.format(amount);
	}
	
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
	
	public static String getFileExtension(String filename){
		return filename.substring(filename.lastIndexOf("."),filename.length());
	}
}
