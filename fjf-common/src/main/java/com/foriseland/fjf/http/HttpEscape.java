package com.foriseland.fjf.http;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @className: HttpEscape 
 * @describe: 过滤和效验Input标签
 * @author: PC  
 * @createTime 2017年12月8日 上午10:01:40
 */
public class HttpEscape {
	
	private static String htmlEncode(String source) {
        if (source == null) {
            return "";
        }
        String html = "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
            case '<':
                buffer.append("&lt;");
                break;
            case '>':
                buffer.append("&gt;");
                break;
            case '&':
                buffer.append("&amp;");
                break;
            case '"':
                buffer.append("&quot;");
                break;
            case 10:
            case 13:
                break;
            default:
                buffer.append(c);
            }
        }
        html = buffer.toString();
        return html;
    }

	/**
	 * @Title: escapeHtml 
	 * @Description: 转义Html内容
	 * @param source 数据内容
	 * @author: wangHaiyang 
	 * @throws
	 */
	public static String escapeHtml(String source){
		if(StringUtils.isEmpty(source)){
			return "";
		}
		source = htmlEncode(source);
		source = StringEscapeUtils.escapeHtml4(source);
		return source;
	}

	/**
	 * @Title: unescapeHtml 
	 * @Description: 反转义内容
	 * @param @param value
	 * @return String    返回类型 
	 * @author: wanghy 
	 * @throws
	 */
	public static String unescapeHtml(String value) {
		value = StringEscapeUtils.unescapeHtml4(value);
		return value;
	}
	
	  //效验
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
	
	public static void main(String[] args) {
		String script = "哈哈<script>alert('345677');</script>update table";
	/*	String sql = "drop name count many";
		boolean ecSQL = sqlValidate(sql);
		System.out.println(ecSQL);*/
		System.out.println(escapeHtml(script));
	}
}