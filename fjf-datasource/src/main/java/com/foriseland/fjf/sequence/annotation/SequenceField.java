package com.foriseland.fjf.sequence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 需要增加序列的字段  
 * @author wangHaiyang
 * @Modified wangHaiyang
 * @date 2016-08-16 下午2:44:50   
 * @version V1.0     
*/ 
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SequenceField {

}
