package com.foriseland.fjf.exception;

/**
 * 
 * @copyright 复华创新平台
 * @description: 基础异常
 * @author wangHaiyang
 * @date 2017-12-01 下午2:01:18
 * @version V1.0
 */
public class BaseExcepton extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String code;
	
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * 创建一个新的实例 BusinessExcepton.
	 * 
	 * @param message
	 */
	public BaseExcepton(String message) {
		super(message);
	}
	
	public BaseExcepton(String message,String code) {
		super(message);
		this.code = code;
	}

	/**
	 * 
	 * 创建一个新的实例 BusinessExcepton.
	 * 
	 * @param msg
	 * @param ex
	 */
	public BaseExcepton(String msg, Throwable ex) {
		super(msg, ex);
	}
	
	public BaseExcepton(String msg, Throwable ex,String code) {
		super(msg, ex);
		this.code = code;
	}
}