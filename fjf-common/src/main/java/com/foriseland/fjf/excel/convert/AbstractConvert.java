package com.foriseland.fjf.excel.convert;

/**
 * Created by stefan on 16-4-28.
 */
public class AbstractConvert implements IConvert {
    @Override
    public <T> String convert(Object value, String params, T record) {
        return convert(value, params);
    }

    @Override
    public String convert(Object value, String params) {
        return String.valueOf(value);
    }
}
