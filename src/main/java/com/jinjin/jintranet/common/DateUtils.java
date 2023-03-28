package com.jinjin.jintranet.common;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static String generateCurrentDate() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(now);
	}

	//Added
	public static String getCurrentDate() {
		Date now = new Date();
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return format.format(now);
	}
	
	public static String getCurrentYear() {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}
	
	public static String getCurrentMonth() {
		return String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
	}
	
	public static String getCurrentDay() {
		return String.valueOf(Calendar.getInstance().get(Calendar.DATE));
	}
	
	public static LocalDateTime toLocalDateTime(String yyyyMMdd) {
		yyyyMMdd += " 00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(yyyyMMdd, formatter);
		return dateTime;
	}
	
	public static LocalDateTime toLocalDateTime(String yyyyMMdd , String HHmmss) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(yyyyMMdd + " " + HHmmss+":00", formatter);
		return dateTime;
	}
}
