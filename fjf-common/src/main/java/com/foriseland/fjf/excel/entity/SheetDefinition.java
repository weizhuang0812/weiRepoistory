package com.foriseland.fjf.excel.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiexiyang on 15/6/25.
 */
public class SheetDefinition implements Serializable {

    private static final long serialVersionUID = 1632248617174732404L;

    //某个
    private String name;

    //中文民称
    private String displayName;

    //字段列表
    private List<FieldDefinition>  fieldList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<FieldDefinition> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldDefinition> fieldList) {
        this.fieldList = fieldList;
    }
}
