package com.foriseland.fjf.excel;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * @className: ImportUtil
 * @describe:导入Excel工具解析
 * @author: wangHaiyang
 * @createTime 2018年3月8日 上午9:56:38
 */
public class ExcelImportUtil {

	/**
	 * @title: importExcel 
	 * @describe: 读取Excel并返回对象
	 * @param excelFile Excel文件对象 
	 * @param responseClass 返回对象类型
	 * @return List<T>
	 * @throws Exception
	 * @author: wangHaiyang 
	 * @createTime 2018年3月8日 上午11:28:05 
	 * @throws
	 */
	public List<Map<String, Object>> importExcel(File excelFile, Object responseClazz ) throws Exception {
		if(null == excelFile) {
			throw new Exception("paramter:[excelFile is null]");
		}
		if(null == responseClazz) {
			throw new Exception("paramter:[responseClass is null]");
		}
		String fileName = excelFile.getName();
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<Map<String, Object>> results = null;
		if ("xlsx".equals(prefix)) {
			System.out.println(1);
			results = this.xlsxExcel(excelFile, responseClazz);
		} else { // xls
			results = this.xlsExcel(excelFile, responseClazz);
		}
		return results;
	}

	/**
	 * 拼装单个obj
	 * 主要针对HSS 结构
	 * @param obj
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> dataToObj(Object obj, HSSFRow row) throws Exception {
		Class<?> rowClazz = obj.getClass();
		Field[] fields = FieldUtils.getAllFields(rowClazz);
		if (fields == null || fields.length < 1) {
			return null;
		}

		// 容器
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 注意excel表格字段顺序要和obj字段顺序对齐 （如果有多余字段请另作特殊下标对应处理）
		for (int j = 0; j < fields.length; j++) {
			resultMap.put(fields[j].getName(), getVal(row.getCell(j)));
		}
		return resultMap;
	}

	/**
	 * 拼装单个obj
	 * 主要针对XSSF 结构
	 * @param obj
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> dataToObj(XSSFRow row,Object obj) throws Exception {
		Class<?> rowClazz = obj.getClass();
		Field[] fields = FieldUtils.getAllFields(rowClazz);
		if (fields == null || fields.length < 1) {
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 注意excel表格字段顺序要和obj字段顺序对齐 （如果有多余字段请另作特殊下标对应处理）
		for (int j = 0; j < fields.length; j++) {
			resultMap.put(fields[j].getName(), getVal(row.getCell(j)));
		}
		System.out.println(resultMap);
		return resultMap;
	}

	private List<Map<String, Object>> xlsxExcel(File file,Object obj) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 容器
		List<Map<String, Object>> ret = Lists.newArrayList();
		// 遍历行 从下标第一行开始（去除标题）
		for (int i = 1; i < sheet.getLastRowNum()+1; i++) {
			XSSFRow row = sheet.getRow(i);
			if (row != null) {
				// 装载obj
				ret.add(dataToObj(row,obj));
			}
		}
		return ret;
	}

	private List<Map<String, Object>> xlsExcel(File file, Object obj) throws Exception {
		// 装载流
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
		HSSFWorkbook hw = new HSSFWorkbook(fs);
		// 获取第一个sheet页
		HSSFSheet sheet = hw.getSheetAt(0);
		// 容器
		List<Map<String, Object>> ret = Lists.newArrayList();

		// 遍历行 从下标第一行开始（去除标题）
		for (int i = 1; i < sheet.getLastRowNum()+1; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				// 装载obj
				ret.add(dataToObj(obj, row));
			}
		}
		return ret;
	}

	/**
	 * 处理val（暂时只处理string和number，可以自己添加自己需要的val类型）
	 * 
	 * @param hssfCell
	 * @return
	 */
	private String getVal(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			return hssfCell.getStringCellValue();
		} else {
			return String.valueOf(hssfCell.getNumericCellValue());
		}
	}

	private String getVal(XSSFCell hssfCell) {
		if (hssfCell == null) {
			return null;
		}
		if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			return hssfCell.getStringCellValue();
		} else {
			return String.valueOf(hssfCell.getRawValue());
//			return String.valueOf(hssfCell.getNumericCellValue());
		}
	}

	public static void main(String[] args) throws Exception {

		ExcelImportUtil ut = new ExcelImportUtil();

		String fileName = "D:\\test.xlsx";
		File f = new File(fileName);
//		List<Map<String, Object>> maps = ut.importExcel(f, new Member());
//		System.out.println("maps:"+maps.size());
	}
}
