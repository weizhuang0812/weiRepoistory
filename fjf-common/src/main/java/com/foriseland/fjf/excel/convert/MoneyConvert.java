package com.foriseland.fjf.excel.convert;

import org.apache.commons.lang3.math.NumberUtils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * moneryConvert转换器，保留两位小树，显示千分位
 */
public class MoneyConvert extends AbstractConvert {
    @Override
    public String convert(Object value, String params) {
        String retVal = "";
        try{
            if(null == value) return "0.0";
            retVal = String.valueOf(value);
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.SIMPLIFIED_CHINESE);
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            //numberFormat.setGroupingUsed(false);//显示千分位
            retVal = numberFormat.format(NumberUtils.toDouble(retVal, 0.0));
        }catch (Exception e){
            e.printStackTrace();
            retVal = "";
        }
        return retVal;
    }
}
