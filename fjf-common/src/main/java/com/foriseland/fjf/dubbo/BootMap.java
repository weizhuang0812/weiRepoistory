package com.foriseland.fjf.dubbo;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class BootMap {
	private  Map<String, Object> contextMap = null;
	
	public static BootMap instance(){
		return new BootMap();
	}
	
	private BootMap(){
		if(contextMap == null){
			contextMap = new HashMap<>();
		}
	}  
	
	public BootMap put(String key,Object value){
		
		this.putVal(key, value);
		return this;
	}
	
	private Map<String, Object> putVal(String key,Object value){
		this.contextMap.put(key, value);
		return contextMap;
	}
	
	public Map<String, Object> getMap(){
		return contextMap;
	}
	
	public String string(){
		String mapJson = JSON.toJSONString(contextMap);
		return mapJson;
	}
	public static void main(String[] args) {
		String json = BootMap.instance().put("1", 1).put("2",2).string();
		System.out.println(json);
	}
}
