package com.foriseland.fjf.lang;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class DateUtil {

	public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE = "yyyy-MM-dd";

	public static String datetimeToStr(final Date date, final String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String dateTimeToStr(final Date date) {
		return DateUtil.datetimeToStr(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String dateToStr(final Date date) {
		return DateUtil.datetimeToStr(date, "yyyy-MM-dd");
	}

	public static String dateToStr(final Date date, String format) {
		return DateUtil.datetimeToStr(date, format);
	}

	public static String getCurrentDate() {
		return new SimpleDateFormat(DATE).format(new Date());
	}

	public static String getCurrentDate(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	public static String getCurrentDatetime() {
		return new SimpleDateFormat(DATETIME).format(new Date());
	}

	public static String getCurrentDatetime(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	public static int getCurrentTimeHashCode() {
		return String.valueOf(System.currentTimeMillis()).hashCode();
	}

	/**
	 * 获得当前时间当天的开始时间，即当前给出的时间那一天的00:00:00的时间
	 *
	 * @param date
	 *            当前给出的时间
	 * @return 当前给出的时间那一天的00:00:00的时间的日期对象
	 */
	public static Date getDateBegin(final Date date) {
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (date != null) {
			try {
				return DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.CHINA).parse(ymdFormat.format(date));
			} catch (ParseException e) {
				System.err.println("DataFromat error");
			}
		}
		return null;
	}

	public static Date getDateEnd(Date date) {
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (date != null) {
			try {
				Date endDate = strToDate(ymdFormat.format(new Date(date.getTime() + 24 * 60 * 60 * 1000)));
				endDate = new Date(endDate.getTime() - 1000);
				return endDate;
			} catch (Exception e) {
				System.err.println("DataFromat error");
			}
		}
		return null;
	}

	public static long getNow() {
		return System.currentTimeMillis();
	}

	public static String getTime() {
		Date d = new Date();
		String re = datetimeToStr(d, "yyyyMMddHHmmssSSS");
		return re;
	}

	public static String getTime(String format) {
		Date d = new Date();
		String re = datetimeToStr(d, format);
		return re;
	}

	public static Date strToFormatDate(final String date, final String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date, new ParsePosition(0));
	}

	public static Date strToDate(final String date) {
		return DateUtil.strToFormatDate(date, "yyyy-MM-dd");
	}

	public static final Date strToDate(final String dateStr, final String format) {
		return DateUtil.strToFormatDate(dateStr, format);
	}

	public static Date strToDateTime(final String date) {
		return DateUtil.strToFormatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date strToDateTime(final String date, final String format) {
		return DateUtil.strToFormatDate(date, format);
	}

	public static Timestamp strToTimestamp(String str) throws Exception {
		if (StringUtils.isEmpty(str)) {
			throw new Exception("转换错误");
		}
		if (str.trim().length() > 10) {
			return new Timestamp(new SimpleDateFormat(DATETIME).parse(str).getTime());
		} else {
			return new Timestamp(new SimpleDateFormat(DATE).parse(str).getTime());
		}
	}

	public static java.sql.Timestamp strToTimestamp(String sDate, String sFormat) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
		Date t = sdf.parse(sDate);
		return new java.sql.Timestamp(t.getTime());

	}

	public static boolean validateExpireDate(final long timeMillis, final long expireTimeMillis) {
		return (getNow() - timeMillis) > expireTimeMillis;
	}

	/**
	 * 获取当天的秒数（00000-86400）
	 *
	 * @return String
	 */
	public static String getHHmmssSSS() {
		Date d = new Date();
		return getHHmmssSSS(d);
	}

	/**
	 * 获取当天的秒数（00000000-86400000）
	 *
	 * @param d
	 * @return String
	 */
	public static String getHHmmssSSS(Date d) {
		int hh = Integer.valueOf(DateUtil.datetimeToStr(d, "HH"));
		int mm = Integer.valueOf(DateUtil.datetimeToStr(d, "mm"));
		int ss = Integer.valueOf(DateUtil.datetimeToStr(d, "ss"));
		int sss = Integer.valueOf(DateUtil.datetimeToStr(d, "SSS"));
		int time = 0;
		if (hh != 0) {
			time = time + hh * 60 * 60 * 1000;
		}
		if (mm != 0) {
			time = time + mm * 60 * 1000;
		}
		if (ss != 0) {
			time = time + ss * 1000;
		}
		if (sss != 0) {
			time = time + sss;
		}
		String str = String.valueOf(time);
		while (str.length() < 8) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * @param date
	 *            日期初始值
	 * @param count
	 *            计算的值
	 * @param status
	 *            对年进行操作 Calendar.YEAR,对月进行计算 Calendar.Month, 对日Calendar.Date
	 * @return 计算后的日期
	 */
	public static Date caculateDate(Date date, Integer count, int status) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(status, calendar.get(status) + count);
		return calendar.getTime();
	}

	/**
	 * 获取给定的年月中有几个自然日
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static int caculateDaysNumber(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 返回指定日期和指定月份的月底的 date 对象
	 *
	 * @param now
	 *            指定的时间
	 * @param month
	 *            指定后几个月的个数
	 * @return 返回指定的时间+几个月的月底
	 */
	public static Date caculateDateByNextBaseMonth(Date now, Integer month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + (month + NumberUtils.INTEGER_ONE));
		cal.set(Calendar.DATE, NumberUtils.INTEGER_ZERO);
		return cal.getTime();
	}

	/**
	 * @param date
	 * @param dateType
	 * @param dateNumber
	 * @return
	 */
	public static Date caculateFinallyDatebyDateType(Date date, int dateType, int dateNumber) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (dateType) {
		case Calendar.YEAR:
			calendar.add(dateType, dateNumber + 1);
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DATE, NumberUtils.INTEGER_ZERO);
			break;
		case Calendar.MONTH:
			calendar.add(Calendar.MONTH, dateNumber + 1);
			calendar.set(Calendar.DATE, NumberUtils.INTEGER_ZERO);
			break;

		case Calendar.DATE:
			calendar.add(Calendar.DATE, dateNumber);
			break;
		default:
			throw new RuntimeException("dateType value error");
		}
		return calendar.getTime();
	}

	/**
	 * @Title: compareTime 
	 * @Description: 
	 * 比较时间大小的方法
	   1、如果返回-1：说明s1<s2
	   2、如果返回0：说明s1=s2
	   3、如果返回1：说明s1>s2
	   4、如果返回2：说明格式不正确
	 * @param @param s1
	 * @param @param s2
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @author: wanghy 
	 * @throws
	 */
	public static int compareTime(String dateTime1, String dateTime2) {
		if(StringUtils.isEmpty(dateTime1) || StringUtils.isEmpty(dateTime1)){
			return 2;
		}
		java.text.DateFormat df = null;
		if (dateTime1.length() == 10 && dateTime2.length() == 10) {
			df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		} else if (dateTime1.length() == 19 && dateTime2.length() == 19) {
			df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else {
			return 2;
		}

		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		try {
			c1.setTime(df.parse(dateTime1));
			c2.setTime(df.parse(dateTime2));
		} catch (java.text.ParseException e) {
			System.err.println("格式不正确");
		}
		int result = c1.compareTo(c2);
		return result;
	}

	public static void main(String args[]) {
		Date now = new Date();
		String nowStr = dateTimeToStr(now);
		System.out.println(nowStr);
		Date fDate = caculateFinallyDatebyDateType(now, 10, 3);
		System.out.println(dateTimeToStr(fDate));

	}
}
