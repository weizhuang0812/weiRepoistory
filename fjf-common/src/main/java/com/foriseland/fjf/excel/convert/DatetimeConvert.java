package com.foriseland.fjf.excel.convert;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiexiyang on 15/6/29.
 */
public class DatetimeConvert extends AbstractConvert {

    @Override
    public String convert(Object value,String params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
