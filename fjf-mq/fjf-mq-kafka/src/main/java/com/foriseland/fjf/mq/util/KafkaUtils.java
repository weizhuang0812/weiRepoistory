package com.foriseland.fjf.mq.util;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.foriseland.fjf.mq.exceptions.KafkaBaseException;

public class KafkaUtils {

    public static final String STRING_SPACE = "";
    public static final String STRING_NULL = null;
    public static final String STRING_COMMA=",";

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    
    /**
     * 获取32位UUID
     * @return
     */
    public static String get32UUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
    
    /** 
     * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回 
     * @param sourceDate 
     * @param formatLength 
     * @return 重组后的数据 
     */  
    public static String frontCompWithZore(long sourceNo,int formatLength) {
    	if(sourceNo == 0 || formatLength == 0){
    		return null;
    	}
//    	if(String.valueOf(sourceNo).length() > formatLength){
//    		return null;
//    	}
    	String newString = String.format("%0"+formatLength+"d", sourceNo);  
    	return  newString;  
    }
    
    /**
	 * @title: isRule
	 * @describe:板块名称_业务名称_Key名称 object-type-key qumao-user-name
	 * @param key
	 *            （存入redis的Key）
	 * @return boolean[true|false]
	 * @author: wangHaiyang
	 * @createTime 2018年1月2日 下午12:00:15 @throws
	 */
	public static boolean isRule(String key) {
		boolean resType = Boolean.FALSE;
		if (StringUtils.isEmpty(key)) {
			return resType;
		}
		String[] keyArray = key.split("-");
		if (null == keyArray) {
			return resType;
		}
		int lens = keyArray.length;
		if (lens < 3) {
			return resType;
		}
		for (String item : keyArray) {
			if (StringUtils.isBlank(item)) {
				return resType;
			}
		}
		return Boolean.TRUE;
	}
	
	public static  void ruleTopic(String topic) throws KafkaBaseException{
		if(KafkaUtils.isRule(topic) == Boolean.FALSE){
			KafkaBaseException ex = new KafkaBaseException("Kafka Topic is not effective,key be similar to [object-model-key]");
			throw ex;
		}
	}
    
    
    public static void main(String[] args) {
		System.out.println(frontCompWithZore(1932312, 5));
		String key = "q-umao-user";
		boolean st = isRule(key);
		System.out.println(st);
	}
}
