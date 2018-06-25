package com.foriseland.fjf.mongdb.exception;

/**
 * 
* 项目名称：framework-common   
* 类名称：ParamException   
* 类描述： 各种请求参数不匹配导致的异常
* 创建人：Administrator   
* 创建时间：2015年2月6日 上午9:48:05   
* 修改人：Administrator   
* 修改时间：2015年2月6日 上午9:48:05   
* 修改备注：   
* @version    
*
 */
public class MongdbException extends Exception {

	private static final long serialVersionUID = 410918530519191826L;

	public MongdbException(){
		super();
	}
	
	public MongdbException(String message) {
		super(message);
	}

	public MongdbException(Throwable cause) {
		super(cause);
	}

	public MongdbException(String message, Throwable cause) {
		super(message, cause);
	}
}
