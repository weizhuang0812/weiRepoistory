package com.foriseland.fjf.excel.convert;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.foriseland.fjf.excel.entity.FieldDefinition;

/**
 * 导出字段转换器，支持自定义
 */
public class ConvertHelper {

    private Map<String, IConvert> commonConvert;

    private AbstractConvert defaultConvert = new AbstractConvert();

    {
        commonConvert = new HashMap<String, IConvert>();
        commonConvert.put("datetime", new DatetimeConvert());
        commonConvert.put("day", new DayConvert());
        commonConvert.put("json", new JsonPropertyConvert());
        commonConvert.put("money", new MoneyConvert());
        commonConvert.put("number", new NumberConvert());
    }

    //提供自定义convert
    //key:convertName  value:convert
    private Map<String, IConvert> externalConvert;

    public void setExternalConvert(Map<String, IConvert> externalConvert) {
        this.externalConvert = externalConvert;
    }

    public <T> Object convert(FieldDefinition field,Object value, T record) {
        String convertName = field.getConvertName();
        String convertParams = field.getConvertParams();
        if(StringUtils.isBlank(convertName)){
            return "";
        }

        IConvert convert = null;
        if(MapUtils.isNotEmpty(externalConvert)){
            convert =  externalConvert.get(convertName);
        }

        if(null == convert){
            convert =  commonConvert.get(convertName);
            if(null == convert){
                convert = defaultConvert;
            }
        }

        try{
            return convert.convert(value, convertParams, record);
        }catch (Exception e){
            return "";
        }
    }
}
