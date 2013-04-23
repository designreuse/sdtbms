package com.bus.util;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public static Date parseDate(String str, String strFormat) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
			Date date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
		}
		String[] formats = new String[]{"yyyy/MM/dd","yyyy-MM-dd","yyyy.MM.dd"};
		for(int i=0; i<formats.length;i++){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(formats[i]);
				Date date = sdf.parse(str);
				return date;
			} catch (ParseException e) {
				
			}
		}
		return null;
	}
}
