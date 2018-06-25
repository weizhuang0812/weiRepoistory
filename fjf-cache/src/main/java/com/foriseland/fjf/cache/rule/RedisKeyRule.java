package com.foriseland.fjf.cache.rule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

public class RedisKeyRule {

	public static boolean isKeyRule(Object value) {
		if (null == value) {
			return Boolean.FALSE;
		}
		if (value instanceof byte[]) {
			return isRule(toByteArray(value));
		}
		if (value instanceof   String[] ){
			String[] arrays = (String[])value;
			for(String item : arrays) {
				boolean res  = isRule(toByteArray(item));
				if(Boolean.FALSE == res) {
					System.err.println("redis key as array,this key not effective,key value-> ["+item+"],redis key be similar to [object-model-key]");
					return Boolean.FALSE; // 数组中存在非法Key，则直接返回False
				}
			}
			return Boolean.TRUE;
		}
		return isRule(value.toString());
	}
	
	public static void main(String[] args) {
		String[]arrays = {"a1","a2"};
		boolean is = isKeyRule(arrays);
		System.out.println(is);
	}

	/**
	 * 对象转数组
	 * 
	 * @param obj
	 * @return
	 */
	private static byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
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
	private static boolean isRule(String key) {
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

	/**
	 * @title: isRule
	 * @describe:板块名称_业务名称_Key名称 object-type-key qumao-user-name
	 * @param key
	 *            （存入redis的Key）
	 * @return boolean[true|false]
	 * @author: wangHaiyang
	 * @createTime 2018年1月2日 下午12:00:15 @throws
	 */
	private static boolean isRule(byte[] key) {
		boolean resType = Boolean.FALSE;
		if (null == key) {
			return resType;
		}
		String newsKey = null;
		try {
			newsKey = new String(key, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		resType = isRule(newsKey);
		return resType;
	}

//	public static void main(String[] args) {
//		String key = "q umao-user";
//		byte[] bytes = key.getBytes();
//		boolean st = isRule(bytes);
//		System.out.println(st);
//	}
}
