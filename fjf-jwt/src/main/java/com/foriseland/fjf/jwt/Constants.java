package com.foriseland.fjf.jwt;

/**
 * @author Administrator
 * @ Created by Administrator on 2014/11/24.
 */
public class Constants {
    /**
     * 根秘钥 @TODO 临时放在这后期读取signature.so文件
     */
    public static final String KEY_MAC = "HmacMD5";
    
    public static final int RETURN_500 = 500; // token is null
    
    public static final int RETURN_501 = 501; // token to array is null
    
    public static final int RETURN_502 = 502; // token to array  less 3
    
    public static final int RETURN_503 = 503; // token error
    
    public static final int RETURN_200 = 200; // token success
    
   
    
}
