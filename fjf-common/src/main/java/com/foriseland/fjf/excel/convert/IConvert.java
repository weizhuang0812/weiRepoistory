package com.foriseland.fjf.excel.convert;

/**
 * 转换器接口，转换入口是方法： String convert(Object value, String params, Object record);
 */
public interface IConvert {

    /**
     * 转换器，若需用到javaBean对象，则可实现此方法
     * @param value
     * @param params
     * @param record
     * @param <T>
     * @return
     */
    <T> String convert(Object value, String params, T record);

    /**
     * 转换器，只需对指定值做转换，则可实现此方法
     * @param value
     * @param params
     * @return
     */
    String convert(Object value, String params);
}
