package com.foriseland.fjf.cache.util;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * Timestamp to java.util.Date类型转换器
 * <p>
 * ClassName: TimestampConverter
 * </p>
 * <p>
 * Description:SpringData默认的时间类型是java.util.Date，而实体类中的时间类型是Timestamp，所以需要转换一下
 * </p>
 * 
 * @author liyiguang
 * @date 2018年2月28日
 */
public class TimestampConverter implements Converter<Date, Timestamp> {

	public Timestamp convert(Date date) {
		if (date != null) {
			return new Timestamp(date.getTime());
		}
		return null;
	}

}