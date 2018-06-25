package com.foriseland.fjf.excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.foriseland.fjf.excel.entity.FieldDefinition;
import com.foriseland.fjf.excel.entity.SheetDefinition;
import com.google.common.collect.Lists;

/**
 * Created by stefan on 16-4-27.
 */
public class ExportConfParser {
    private String sheetConfFilePath; //配置文件路径

    private Map<String,SheetDefinition> sheetMapper = new HashMap<>(); //解析配置文件的结果集

    public void setSheetConfFilePath(String sheetConfFilePath) {
        this.sheetConfFilePath = sheetConfFilePath;
        parseConfFile();
    }

    public Map<String, SheetDefinition> getSheetMapper() {
        return this.sheetMapper;
    }

    private void parseConfFile(){
        //加載配置文件
        Document dom = null;
        try {
            String text = IOUtils.toString(ExportConfParser.class.getResourceAsStream(this.sheetConfFilePath));

            dom = DocumentHelper.parseText(text);
            Element sheetsElement = dom.getRootElement();
            List<Element> sheetNodeList = sheetsElement.elements("sheet");
            for(Element sheetNode : sheetNodeList){
                SheetDefinition sheetDefinition = new SheetDefinition();
                List<Element> fieldNodeList = sheetNode.element("fields").elements("field");
                List<FieldDefinition> fieldList = Lists.newLinkedList();
                for(Element element :fieldNodeList){
                    String name = element.attributeValue("name");
                    String property = element.attributeValue("property");
                    String convert = element.attributeValue("convert");
                    String convertName = null;
                    String convertParams = null;
                    if(StringUtils.isNotBlank(convert)){
                        String[] tempArr = convert.split(":");
                        if(tempArr.length == 1){
                            convertName = tempArr[0];
                        }
                        if(tempArr.length == 2){
                            convertName = tempArr[0];
                            convertParams = tempArr[1];
                        }
                    }
                    fieldList.add(new FieldDefinition( name, property, convertName, convertParams));
                }
                sheetDefinition.setFieldList(fieldList);

                String sheetName = sheetNode.attributeValue("name");
                sheetDefinition.setName(sheetName);

                String sheetDisplayName = sheetNode.attributeValue("displayName");
                sheetDefinition.setDisplayName(sheetDisplayName);

                sheetMapper.put(sheetName, sheetDefinition);
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
