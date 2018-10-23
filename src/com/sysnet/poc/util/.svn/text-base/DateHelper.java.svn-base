/**
 * @author sui_zhiwei
 * @version1.0
 * 日期工具类
 */
package com.sysnet.poc.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {
	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static Date currentlyDate() {
		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		return currentDate;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param formate
	 * @return
	 */
	public static String formatDate(Date date, String formate) {
		SimpleDateFormat formatter = new SimpleDateFormat(formate);
		String dateSte = formatter.format(date);
		return dateSte;
	}

	/**
	 * 格式化日期
	 * 
	 * @param dateStr
	 * @return 例子：输入字符串 Mon Nov 02 09:48:06 CST 2009 输出 2009-11-02
	 */
	@SuppressWarnings("deprecation")
	public static String convertEnDateToCnDate(String dateStr) {

		SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd");
		matter.setTimeZone(TimeZone.getTimeZone("GMT-6:00"));
		if (dateStr.contains("CST") && matter != null) {
			Date date = new Date(dateStr);
			return matter.format(date);
		}
		return dateStr;

	}

	/**
	 * 格式化日期
	 * 
	 * @param dateStr
	 * @return 例子：输入字符串 Mon Nov 02 09:48:06 CST 2009 输出 2009-11-02 09:48:06
	 * 
	 *         CST （美国）中部标准时间 CDT （美国）中部夏令时
	 */

	@SuppressWarnings("deprecation")
	public static String convertEnDateToCnDateTime(String dateStr) {

		if ("null".equals(dateStr) || dateStr == null) {
			return "";
		}
		SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (dateStr.contains("CST") && matter != null) {
			matter.setTimeZone(TimeZone.getTimeZone("GMT-6:00"));
			Date date = new Date(dateStr);
			return matter.format(date);
		} else if (dateStr.contains("CDT") && matter != null) {
			matter.setTimeZone(TimeZone.getTimeZone("GMT-5:00"));
			Date date = new Date(dateStr);
			return matter.format(date);
		} else {
			// System.out.println(dateStr);
			return dateStr;
		}
	}

	/**
	 * 将utl.Data转换成sql.Data
	 * 
	 * @param date
	 * @return
	 */
	public static java.sql.Date getSqlDate(java.util.Date date) {
		java.sql.Date sqlDate = null;
		if (date != null)
			sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}

	/**
	 * 返回sql类型当前时间
	 * 
	 * @return
	 */
	public static Timestamp sqlCurrentlyDate() {
		Calendar cal = Calendar.getInstance();
		Timestamp cqd = new Timestamp(cal.getTime().getTime());
		return cqd;
	}

	public static Timestamp convertToTimestamp(java.util.Date date) {
		if (date != null) {
			return new Timestamp(date.getTime());
		} else {
			return null;
		}

	}

	/**
	 * 取当前时间
	 * 
	 * @return
	 */
	public static int getTimeI() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
	}

	/**
	 * 取当前小时
	 * 
	 * @return
	 */
	public static int getHour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);

	}
}
