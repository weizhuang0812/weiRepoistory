package com.foriseland.fjf.dubbo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.dubbo.annotation.SoaResponse;
import com.foriseland.fjf.exception.RequestException;
import com.foriseland.fjf.log.LogMgr;
import com.foriseland.fjf.rpc.storage.Request;
import com.foriseland.fjf.rpc.storage.Response;

/**
 * @ClassName: com.foriseland.fjf.aop
 * @Description:AopApiParameterValidation
 * @author: wangHaiyang
 * @date 2017/12/01
 */
@Component
@Aspect
@Order(0)
public class APIControllerProceedingJoinPoint {

	@Pointcut("@annotation(com.foriseland.fjf.dubbo.annotation.SoaResponse)")
	public void annotation() {
	}
	
	public Response<?> initResponse(Response<?> pointData){
		if(pointData == null){
			pointData = Response.create();
		}
		return pointData;
	}
	
	@Around("annotation() && @annotation(annotationObj)")
	public Response<?> around(ProceedingJoinPoint joinPoint, SoaResponse annotationObj)
			throws InstantiationException, IllegalAccessException {
		Response<?> response = null;
		try {
			/*for (int i = 0; i < joinPoint.getArgs().length; i++) {
				// System.out.println("AOP切入参数:"+joinPoint.getArgs()[i]);

			}*/
			Request<?> request = null;
			String paramClassName = joinPoint.getArgs()[0].getClass().getSimpleName();
			if (paramClassName.equals(Request.class.getSimpleName())) {
				request = (Request<?>) joinPoint.getArgs()[0];
			}
			StringBuilder logs = new StringBuilder();
			logs.append("{").append("rpc method:").append(joinPoint.getSignature().getName())
			.append(",").append("cip:"+request.getIp()).append(",").append("cid:").append(request.getSid())
			.append(",").append("reqTime:").append(request.getReqTime()).append("]");
			LogMgr.sysInfo(logs.toString());
			Object result = joinPoint.proceed();
			response = (Response<?>)(result);
			long requestTime = request.getReqTime();
			long endTime = System.currentTimeMillis();
			response.setPerformance((endTime-requestTime));
			return response;
		} catch (RequestException e) {
			LogMgr.error("[dubbo -> around -> ServiceException]", e);
			response = initResponse(response);
			response.setErrorMsg(e.getMessage());
			response.setErrorCode("500");
			return response;
		} catch (Throwable throwable) {
			// throwable.printStackTrace();
			LogMgr.error("[dubbo -> around -> ServiceException -> Throwable]", throwable);
			//return this.convertResult(annotationObj.resultClassType(), throwable);
			response = initResponse(response);
			response.setErrorMsg(throwable.getMessage());
			response.setErrorCode("500");
			return response;
		}
	}
}