package com.foriseland.fjf.excel.convert;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.DecimalFormat;

/**
 * Created by stefan on 16-8-5.
 * 数字格式化，支持保留两位小数，百分比格式化，取整等
 * 取整(默认为0) number:0
 * 保留两位小数 number:0.00
 * 百分比 number:0.00%
 */
public class NumberConvert extends AbstractConvert {
    @Override
    public String convert(Object value, String params) {
        if(null == value || StringUtils.isBlank(value.toString())){
            value = 0;
        }
        if(StringUtils.isBlank(params)){ //默认转整形
            return String.valueOf(NumberUtils.toLong(value.toString(), 0l));
        }

        try {//保留两位小数，或者百分比，格式自定义
            Double retValue = Double.parseDouble(value.toString());
            DecimalFormat decimalFormat = new DecimalFormat(params);
            return decimalFormat.format(retValue);
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }
}
