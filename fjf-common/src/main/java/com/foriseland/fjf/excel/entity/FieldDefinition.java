package com.foriseland.fjf.excel.entity;

import java.io.Serializable;

/**
 * Created by xiexiyang on 15/6/25.
 */
public class FieldDefinition  implements Serializable{

    private static final long serialVersionUID = -4269350903354555136L;
    //中文名称
    private String name;
    //对应的属性值
    private String property;
    //转换器名称
    private String convertName;
    //转换器参数
    private String convertParams;
    public FieldDefinition( ) {
        super();
    }

    public FieldDefinition( String name, String property) {
        this.name = name;
        this.property = property;
    }

    public FieldDefinition( String name, String property, String convertName, String convertParams) {
        this.name = name;
        this.property = property;
        this.convertName = convertName;
        this.convertParams = convertParams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getConvertName() {
        return convertName;
    }

    public void setConvertName(String convertName) {
        this.convertName = convertName;
    }

    public String getConvertParams() {
        return convertParams;
    }

    public void setConvertParams(String convertParams) {
        this.convertParams = convertParams;
    }
}
