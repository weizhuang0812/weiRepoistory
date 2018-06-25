package com.foriseland.fjf.excel.convert;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiexiyang on 15/6/29.
 */
public class DayConvert extends AbstractConvert {

    @Override
    public String convert(Object value,String params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String retVal = "";
        try{
            if (value instanceof Date){
                retVal = sdf.format(value);
            }else if(value instanceof Long){
                if(value!=null && StringUtils.isNotBlank(value.toString())) {
                    retVal = sdf.format(new Date(Long.valueOf(value.toString())));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            retVal = "";
        }

        return retVal;
    }
}
