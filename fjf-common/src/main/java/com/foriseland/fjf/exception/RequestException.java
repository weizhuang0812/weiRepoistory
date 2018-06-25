package com.foriseland.fjf.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @className: com.foriseland.fjf.exception
 * @description:
 * @author: wangHaiyang
 * @date 2017/12/01
 */
public class RequestException extends RuntimeException {
	private static final long serialVersionUID = 8438972971646946445L;

	protected String code;

    public static final String REQUEST_PARAM_SIZE_ERROR = "REQUEST_PARAM_NUMBER_ERROR";
    public static final String REQUEST_PARAM_VALUE_ERROR = "REQUEST_PARAM_VALUE_ERROR";
    public static final String SERVICE_DATA_FAIL_ERROR = "SERVICE_DATA_FAIL_ERROR";
    public static final String REDIS_SERVICE_LOCK_ERROR = "REDIS_SERVICE_LOCK_ERROR";
    public static final String SYS_ERROR = "SYS_ERROR";

    public static Map<String, String> ERROR_MAP = new HashMap<String, String>();

    static {
        ERROR_MAP.put(REQUEST_PARAM_SIZE_ERROR, "必填的请求参数列表有缺失.");
        ERROR_MAP.put(REQUEST_PARAM_VALUE_ERROR, "参数值异常.");
        ERROR_MAP.put(SERVICE_DATA_FAIL_ERROR, "程序处理中数据有效性异常.");
        ERROR_MAP.put(REDIS_SERVICE_LOCK_ERROR, "当前数据正在处理,请稍后");
        ERROR_MAP.put(SYS_ERROR, SYS_ERROR);
    }


    public RequestException(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return ERROR_MAP.get(code);
    }

    public String getCode() {
        return code;
    }

}