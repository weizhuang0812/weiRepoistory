package com.foriseland.fjf.dubbo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: com.foriseland.fjf.annotation
 * @Description: dubbo APIController 
 * @author: wangHaiyang
 * @date 2017/12/01
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SoaResponse {
//    Class<? extends ServerResultConvert> resultClassType() default ApiResult.class;
}
