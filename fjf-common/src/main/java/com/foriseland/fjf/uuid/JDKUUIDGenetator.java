/**
 * 
 */
package com.foriseland.fjf.uuid;

import java.util.UUID;

/**   
 *    
 * 项目名称：framework-common   
 * 类名称：JDKUUIDGenetator   获取UUID | getUUID()单个UUID 
 * 创建人：Administrator   
 * 创建时间：2015年2月12日 下午1:51:35   
 * 修改人：Administrator   
 * 修改时间：2015年2月12日 下午1:51:35   
 * 修改备注：   
 * @version    
 *    
 */
public class JDKUUIDGenetator {
    public JDKUUIDGenetator() {   
    }   
  
    public static String getUUID() {   
        UUID uuid = UUID.randomUUID();   
        String str = uuid.toString();   
        // 去掉"-"符号   
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);   
        return str+","+temp;   
    }   
    //获得指定数量的UUID   
    public static String[] getUUID(int number) {   
        if (number < 1) {   
            return null;   
        }   
        String[] ss = new String[number];   
        for (int i = 0; i < number; i++) {   
            ss[i] = getUUID();   
        }   
        return ss;   
    }   
  
    public static void main(String[] args) {   
        String[] ss = getUUID(10);   
        for (int i = 0; i < ss.length; i++) {   
            System.out.println("ss["+i+"]====="+ss[i]);   
        }   
    }   
}  