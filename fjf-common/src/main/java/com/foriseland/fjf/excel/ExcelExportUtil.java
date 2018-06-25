package com.foriseland.fjf.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.foriseland.fjf.excel.convert.ConvertHelper;
import com.foriseland.fjf.excel.entity.FieldDefinition;
import com.foriseland.fjf.excel.entity.SheetDefinition;
import com.foriseland.fjf.excel.utils.QdBeanUtils;

/**
 * Created by stefan on 16-4-27.
 */
public class ExcelExportUtil {

    private ConvertHelper convertHelper;

    private ExportConfParser parser;

    public void setParser(ExportConfParser parser) {
        this.parser = parser;
    }

    public void setConvertHelper(ConvertHelper convertHelper) {
        this.convertHelper = convertHelper;
    }

    /**
     * http请求导出
     * @param response
     * @param dataList
     * @param sheetName
     * @param <T>
     * @throws IOException
     */
    public <T> void exportForServletResponse(HttpServletResponse response, List<T> dataList, String sheetName) throws IOException {
        SheetDefinition sheetDefinition = StringUtils.isBlank(sheetName)?getRandomSheetDefinition():getSheetDeinitionByName(sheetName);

        if(null == sheetDefinition) return;

        String fileName =  dealStr(sheetDefinition.getDisplayName()) + ".xls";
        response.setHeader("Content-Disposition",
                "attachment;filename=" +URLEncoder.encode(fileName, "utf-8")+ ";filename*=UTF-8''" + URLEncoder.encode(fileName, "utf-8"));
        response.setContentType("application/octet-stream");

        OutputStream outputStream = response.getOutputStream();
        export(outputStream, dataList, sheetDefinition);
        outputStream.flush();
        outputStream.close();
        response.flushBuffer();
    }

    /**
     * 未指定sheetName，则随机取其中一个，一般都是第一个
     * @param response
     * @param dataList
     * @param <T>
     * @throws IOException
     */
    public <T> void exportForServletResponse(HttpServletResponse response, List<T> dataList) throws IOException {
        exportForServletResponse(response, dataList, null);
    }

    /**
     * 导出
     * @param outputStream 导出输出流
     * @param dataList 导出内容
     * @param sheetDefinition 导出表格定义信息
     */
    public <T> void export(OutputStream outputStream, List<T> dataList, SheetDefinition sheetDefinition) throws IOException{
        if(null == convertHelper){
            convertHelper = new ConvertHelper();
        }
        if(null == sheetDefinition){
            return;
        }
        Workbook workbook = genWorkBook(sheetDefinition, dataList);
        workbook.write(outputStream);
    }

    /**
     * 未指定sheetName，则随机取其中一个，一般都是第一个
     * @param outputStream
     * @param dataList
     * @param <T>
     * @throws IOException
     */
    public <T> void export(OutputStream outputStream, List<T> dataList) throws IOException{
        export(outputStream, dataList, getRandomSheetDefinition());
    }

    /**
     * 随机获取一个sheetName，一般是第一个
     * @return
     */
    private SheetDefinition getRandomSheetDefinition(){
        Map<String,SheetDefinition> sheetMapper = parser.getSheetMapper();
        Iterator<Map.Entry<String, SheetDefinition>> iterator = sheetMapper.entrySet().iterator();
        if(iterator.hasNext()){
            return iterator.next().getValue();
        }
        return null;
    }

    private SheetDefinition getSheetDeinitionByName(String sheetName){
        Map<String,SheetDefinition> sheetMapper = parser.getSheetMapper();
        return sheetMapper.get(sheetName);
    }

    public <T> Workbook genWorkBook(SheetDefinition sheetDefinition,List<T> recordsAll){
        Workbook wb = new SXSSFWorkbook(1000);
        Sheet sheet = wb.createSheet(sheetDefinition.getDisplayName());

        createTitle(sheet, sheetDefinition);
        createContent(wb, sheet, recordsAll, sheetDefinition);
        return wb;
    }


    private <T> void createContent(Workbook wb,Sheet sheet,  List<T> records,SheetDefinition sheetDefinition) {
        //创建样式
        CellStyle dateStyle = wb.createCellStyle();
        //日期格式
        dateStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy/MM/DD HH:MM:SS"));

        int rowIndex = 1;
        for( T  record : records) {
            if(record == null) continue;
            Row row = sheet.createRow(rowIndex++);
            int colIndex = 0;
            for (short i = 0; i < sheetDefinition.getFieldList().size(); i++) {
                FieldDefinition field = sheetDefinition.getFieldList().get(i);
                String property = field.getProperty();
                Object value = QdBeanUtils.getInstance().getKeyValue(record, property);

                value = value == null ? "":value;
                //如果有过滤器，走过滤器
                if(StringUtils.isNotBlank(field.getConvertName())){
                    value = convertHelper.convert(field,value,record);
                }
                value = value == null ? "":value;

                createCell(row, colIndex++, value);
            }
        }
    }

    private void createTitle(Sheet sheet,SheetDefinition sheetDefinition){
        //生成标题行
        Row titleRow = sheet.createRow(0);
        for (short i = 0; i < sheetDefinition.getFieldList().size(); i++) {
            createCell(titleRow, i, sheetDefinition.getFieldList().get(i).getName());
        }
    }
    /**
     * 创建单元格并设置样式
     * @param row
     * @param column
     * @param value
     */
    private void createCell( Row row, int column,Object value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(String.valueOf(value));
    }

    /**
     * 创建单元格并设置样式-时间
     * @param row
     * @param column
     * @param value
     */
    private void createCell( Row row, int column,Date value,CellStyle dateStyle) {
        Cell cell = row.createCell(column);
        cell.setCellStyle(dateStyle);
        cell.setCellValue(value);
    }

    /**
     * 创建单元格并设置样式-金额
     * @param row
     * @param column
     * @param value
     */
    private void createCell( HSSFRow row, int column,Double value) {
        Cell cell = row.createCell(column);

        cell.setCellValue(value);
    }

    /**
     * 创建单元格并设置样式-数字
     * @param row
     * @param column
     * @param value
     */
    private void createCell( HSSFRow row, int column,Integer value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    private String dealStr(String str){
        if(StringUtils.isBlank(str)){
            return "未定义表格";
        }
        return str;
    }
}
