package com.foriseland.fjf.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.foriseland.fjf.constants.HttpStatus;
import com.foriseland.fjf.exception.ServiceException;
import com.foriseland.fjf.uuid.ShortUUIDGenerator;
@SuppressWarnings("unused")
public class RestToJson implements Serializable{

	private static final long serialVersionUID = -8246151282109944075L;
    private String errorMsg = null;
    private String sid;
    private int status = 200;
    private Object data;
    private Object paramter;
    
    /*分页相关属性*/
    private String pageNum;
	private String pageSize;
	private String totalSize;
    
    private RestToJson(int status,String errorMsg, Object data) {
    	this.status = status;
        this.errorMsg = errorMsg;
        this.data = data;
    }
    
    private RestToJson(Object data,Object paramter) {
    	this.status = HttpStatus.OK.getStatusCode();
        this.errorMsg = "";
        this.data = data;
        this.pageNum = "0";
        this.pageSize = "0";
        this.totalSize = "0";
        this.paramter = paramter;
    }
    
    public static  String convert(Object data,Object paramter,int pageNum,Integer pageSize,long totalRecordCount) {
    	RestToJson bean = new RestToJson(data,paramter);
    	bean.pageNum = String.valueOf(pageNum);
    	bean.pageSize = String.valueOf(pageSize);
    	bean.totalSize = String.valueOf(totalRecordCount);
    	bean.paramter = paramter;
    	return FastJsonUtil.parseToJSON(bean);
    }
    
    public static  String convert(Object data,Object paramter) {
        if ((data instanceof Throwable) == Boolean.FALSE) {
        	return FastJsonUtil.parseToJSON(new RestToJson(data,paramter));
        }
        
        if(data instanceof ServiceException){
        	return FastJsonUtil.parseToJSON(new RestToJson((HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()), ((ServiceException) data).getMessage(), null));
        }
        
        return FastJsonUtil.parseToJSON(new RestToJson(HttpStatus.SERVICE_UNAVAILABLE.getStatusCode(), ((Throwable) data).getMessage(), null));
    }
    
	/**
	 * 接口调用失败，返回异常信息
	 */
	public static String exception(Exception e,Object paramter) {
		RestToJson result = new RestToJson();
		result.data = "";
		result.setStatus(500);
		result.setErrorMsg(e.getMessage());
		result.setSid(ShortUUIDGenerator.get32UUID()); // 服务端处理的标识
		result.setPageNum("0");
		result.setPageSize("0");
		result.setTotalSize("0");
		result.setParamter(paramter);
		return FastJsonUtil.parseToJSON(result);
	}
	
	/**
	 * 接口调用失败，返回异常信息
	 */
	public static  String exception(String message,Object paramter) {
		RestToJson result = new RestToJson();
		result.data = "";
		result.setStatus(500);
		result.setErrorMsg(message);
		result.setSid(ShortUUIDGenerator.get32UUID()); // 服务端处理的标识
		result.setPageNum("0");
		result.setPageSize("0");
		result.setTotalSize("0");
		result.setParamter(paramter);
		return FastJsonUtil.parseToJSON(result);
	}

    public int getStatus() {
		return status;
	}
    
	public String getSid() {
		return sid;
	}

	public Object getParamter() {
		return paramter;
	}

	public void setParamter(Object paramter) {
		this.paramter = paramter;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public RestToJson() {
    }

    public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Object getData() {
		return data;
	}
	
    public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}

	public static void main(String[] args) {
    	List<Map<String,Object>> listMaps = new ArrayList<>();
    	for(int i=0;i<5;i++){
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put("uid",ShortUUIDGenerator.get32UUID());
    		map.put("class",ShortUUIDGenerator.class);
    		listMaps.add(map);
    	}
    	Map<String,Object> map = new HashMap<String,Object>();
		map.put("uid",ShortUUIDGenerator.get32UUID());
		map.put("class",ShortUUIDGenerator.class);
		Map<String,Object> mapParamter = new HashMap<String,Object>();
		mapParamter.put("id",ShortUUIDGenerator.get32UUID());
		mapParamter.put("name",ShortUUIDGenerator.get32UUID());
		String res =RestToJson.convert(map,mapParamter);
		System.out.println(res);
	}
}
