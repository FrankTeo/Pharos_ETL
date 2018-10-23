package com.sysnet.poc.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static final Date getDate(int year, int month, int day, int hour, int minute) {
		Calendar cal = new GregorianCalendar(year, intToCalendarMonth(month), day, hour, minute);
		return cal.getTime();
	}

	private static int intToCalendarMonth(int month) {
		if (month == 1) {
			return Calendar.JANUARY;
		} else if (month == 2) {
			return Calendar.FEBRUARY;
		} else if (month == 3) {
			return Calendar.MARCH;
		} else if (month == 4) {
			return Calendar.APRIL;
		} else if (month == 5) {
			return Calendar.MAY;
		} else if (month == 6) {
			return Calendar.JUNE;
		} else if (month == 7) {
			return Calendar.JULY;
		} else if (month == 8) {
			return Calendar.AUGUST;
		} else if (month == 9) {
			return Calendar.SEPTEMBER;
		} else if (month == 10) {
			return Calendar.OCTOBER;
		} else if (month == 11) {
			return Calendar.NOVEMBER;
		} else if (month == 12) {
			return Calendar.DECEMBER;
		} else {
			return Calendar.JANUARY;
		}
	}

	/**
	 * 返回当前的 SQLDate 类型日期
	 * 
	 * @return
	 */
	public static java.sql.Date getNowSqlDate() {
		java.util.Date now = new java.util.Date();
		return new java.sql.Date(now.getTime());
	}
}
