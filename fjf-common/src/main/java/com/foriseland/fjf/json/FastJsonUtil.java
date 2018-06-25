package com.foriseland.fjf.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class FastJsonUtil {
	
	private static SerializeConfig mapping = new SerializeConfig();
	private static SimpleDateFormatSerializer formatSerializer = new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 从JSON字符串中解析出java对象
	 * @param jsonStr JSON字符串
	 * @param clazz 转换的javaBean
	 * @author:wangHaiyang
	 * @createTime:2012-7-8
	 * @param clazz java类
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseToClass(String jsonStr,Class<?> clazz){
		T javaObject = (T)JSON.toJavaObject(JSON.parseObject(jsonStr), clazz);
		return javaObject;
	}
	
	/**
	 * parseJSONString
	 * 将Object对象转换成JSON,自动转换为yyyy-MM-dd HH:mm:ss格式化时间的数据
	 * @param object 任意类型的java数据结构
	 * @author wangHaiyang
	 * @date 2013-6-6 下午10:20:47
	 * @exception
	 * @since  1.0
	 */
	public static String parseToJSON(Object object){
		return JSON.toJSONString(object,configMapping()); // features
	}
	
	/**
	 * parseUnicodeJSON
	 * 将JSON里面的中文替换为Unicode编码形式
	 * @param object
	 * @author wangHaiyang
	 * @date 2013-7-23 上午9:52:17
	 * @since  1.0
	 */
	public static String parseUnicodeJSON(Object object){
		return JSON.toJSONString(object, SerializerFeature.BrowserCompatible);
	}
	
	/**
	 * parseJSONString
	 * 动态选择将对象转换为JSON封装的参数内容 
	 * 如 map.put("name","张三'");map.put("text","'	%&‘哈哈’'行业'''");
	 *   SimplePropertyPreFilter filter = new SimplePropertyPreFilter(mapEntity.getClass(), "text","");
	 * parseJSON:{"text":"'	%&‘哈哈’'行业'''"}
	 * SimplePropertyPreFilter
	 * @author wangHaiyang
	 * @date 2013-7-22 下午4:49:27
	 * @since  1.0
	 */
	public static String parseJSONString(Object object,SimplePropertyPreFilter filter){
		return JSON.toJSONString(object, filter);
	}
	
	/**
	 * parseJSONString
	 * 将Object对象转换成JSON
	 * @param object 任意类型的java数据结构
	 * @param formatDate 如果需要日期转换,请给定日期转换的格式
	 * @author wangHaiyang
	 * @date 2013-6-6 下午10:20:47
	 * @exception
	 * @since  1.0
	 */
	public static String parseJSONString(Object object,String formatDate){
		return JSON.toJSONString(object,configMapping(formatDate));
	}
	
	/**
	 * getListJSONToJava
	 * 将JSON对象转换成Java对象,并通过list集合
	 * @param jsonString JSON字符串
	 * @param pojoClass java 对象类型
	 * @author wangHaiyang
	 * @date 2013-6-6 下午10:20:47
	 * @exception
	 * @since  1.0
	 */
	public static List<?> getListJSONToJava(String jsonString, Class<?> pojoClass){
		JSONArray jsonArray = JSONArray.parseArray(jsonString);
		JSONObject jsonObject;
		Object pojoValue;
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSON.toJavaObject(jsonObject, pojoClass);
			list.add(pojoValue);
		}
		return list;
	}
	
	/**
	 * 将JSON数据格式转换为HashMap形式
	 * @param jsonString
	 * @return
	 */
	public static Map<Object,Object> getMapJSON(String jsonString) {
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		Map<Object, Object> parseJSONMap= new  LinkedHashMap<Object,Object>(jsonObject);
		return parseJSONMap;
	}
	
	private static SerializeConfig configMapping(){
		mapping.put(Date.class, formatSerializer);
		return mapping;
	}
	
	private static SerializeConfig configMapping(String format){
		SerializeConfig mapping = new SerializeConfig();
		mapping.put(Date.class, new SimpleDateFormatSerializer(format));
		return mapping;
	}
	
	/**
	 * filterProperty
	 * 动态选择将对象转换为JSON封装的参数内容 
	 * 如 map.put("name","张三'");map.put("text","'	%&‘哈哈’'行业'''");
	 *   SimplePropertyPreFilter filter = new SimplePropertyPreFilter(mapEntity.getClass(), "text","");
	 * parseJSON:{"text":"'	%&‘哈哈’'行业'''"}
	 * SimplePropertyPreFilter
	 * @author wangHaiyang
	 * @date 2013-7-22 下午4:49:27
	 * @since  1.0
	 */
	public static SimplePropertyPreFilter filterProperty(Class<?> className,String ... param){
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(className,param);
		return filter;
	}
	
	@SuppressWarnings("unused")
	private static final SerializerFeature[] features = { SerializerFeature.WriteMapNullValue, // 输出空置字段
        SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
        SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
        SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
        SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
     };
  
}
