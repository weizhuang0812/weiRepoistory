package com.foriseland.fjf.lang;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateEditor extends PropertyEditorSupport{
	
	/**
	 * spring日期转换工具，将前台传过来的字符串表示的日期转换成日期
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			if(StringUtils.isNotBlank(text)){
				date = format.parse(text);
			}
		} catch (ParseException e) {
			format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = format.parse(text);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		setValue(date);
	}
	
}
