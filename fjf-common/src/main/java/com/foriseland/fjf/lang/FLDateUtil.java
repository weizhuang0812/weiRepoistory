package com.foriseland.fjf.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;


public class FLDateUtil {

	public static final long DAY_TIME_AMOUNT = 24 * 60 * 60 * 1000L;
	
    /**
     * 返回指定那天开始(0时0分0秒0毫秒)的日期时间值
     * @return
     */
    public static long now(){
        return System.currentTimeMillis();
    }
    public static long startOfADay(){
        return startOfADay(null);
    }
    public static long startOfADay(Date dt){
        String smpNowDt = dateOfADay(dt);
        smpNowDt += "000000000";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        try {
            return sdf.parse(smpNowDt).getTime(); 
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static long endOfADay(){
        return endOfADay(null);
    }
    public static long endOfADay(Date dt){
        String smpNowDt = dateOfADay(dt);
        smpNowDt += "235959999";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        try {
            return sdf.parse(smpNowDt).getTime(); 
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static int getYear(){
        DateTime dt = new DateTime();
        return dt.getYear();
    }
    
    public static Long startOfMonth(){
        DateTime ndt = new DateTime();
        DateTime dt = new DateTime(ndt.getYear(), ndt.getMonthOfYear(), 1, 1, 0, 0, 0); // 本月初
        return dt.getMillis();
    }
    
    public static Long endOfMonth(){
        DateTime ndt = new DateTime();
        DateTime dt = new DateTime(ndt.getYear(), ndt.getMonthOfYear()+1, 1, 1, 0, 0, 0); // 本月初
        return dt.getMillis() - 1;
    }
    
    
    public static Long startOfYear(){
        DateTime ndt = new DateTime();
        DateTime dt = new DateTime(ndt.getYear(), 1, 1, 1, 0, 0, 0); // 本月初
        return dt.getMillis();
    }
    
    public static Long endOfYear(){
        DateTime ndt = new DateTime();
        DateTime dt = new DateTime(ndt.getYear()+1, 1, 1, 1, 0, 0, 0); // 本月初
        return dt.getMillis() - 1;
    }
    
    public static int getMothOfYear(){
        DateTime dt = new DateTime();
        return dt.getMonthOfYear();
    }

    public static String dateOfADay(Date dt) {
        dt = dt == null ? new Date() : dt;
        String smpNowDt = DateFormatUtils.ISO_DATE_FORMAT.format(dt);
        smpNowDt = smpNowDt.replace("-", "");
        return smpNowDt;
    }
    public static String strOfADay() {
        return dateOfADay(null);
    }
    
    public static void main(String[] args) {
//        System.out.println(QDDateUtil.startOfADay());
//        System.out.println(QDDateUtil.endOfADay());
//        System.out.println(QDDateUtil.startOfADay(new Date("2014-09-11")));
//        System.out.println(QDDateUtil.endOfADay(new Date("2014-09-11")));
        int i =2003;
        long d = 13220100526L;
        System.out.println(new Date(d));
        System.out.println(i / 20 + (i % 20 > 0 ? 1 : 0));
    }
    
}