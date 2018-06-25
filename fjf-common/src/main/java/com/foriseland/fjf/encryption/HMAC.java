package com.foriseland.fjf.encryption;

import com.google.common.base.Strings;  
import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;  
  
import javax.crypto.KeyGenerator;  
import javax.crypto.Mac;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;  
import java.security.NoSuchAlgorithmException;  

/**
 * @className: HMAC 
 * @describe: HMACSHA25算法
 * <p>
 * HMAC是密钥相关的哈希运算消息认证码，HMAC运算利用哈希算法，以一个密钥和一个消息为输入，生成一个消息摘要作为输出。
 * </p>
 * <span>
 * 算法公式 ： HMAC（K，M）=H（K⊕opad∣H（K⊕ipad∣M））
 * 定义HMAC需要一个加密用散列函数（表示为H）和一个密钥K。
 * 我们假设H是一个将数据块用一个基本的迭代压缩函数来加密的散列函数。
 * 我们用B来表示数据块的字长。（以上说提到的散列函数的分割数据块字长B=64），
 * 用L来表示散列函数的输出数据字长（MD5中L=16(128位),SHA—1中L=20(160位)）。
 * 鉴别密钥的长度可以是小于等于数据块字长的任何正整数值。
 * 应用程序中使用的密钥长度若是比B大，则首先用使用散列函数H作用于它，然后用H输出的L长度字符串作为在HMAC中实际使用的密钥。
 * 一般情况下，推荐的最小密钥K长度是L个字长。（与H的输出数据长度相等）。
 * </span>
 * @author: wangHaiyang  
 * @createTime 2017年12月18日 上午11:56:21
 */
@SuppressWarnings("restriction")
public class HMAC {  
    /** 
     * 定义加密方式 
     * MAC算法可选以下多种算法 
     * <pre> 
     * HmacMD5 
     * HmacSHA1 
     * HmacSHA256 
     * HmacSHA384 
     * HmacSHA512 
     * </pre> 
     */  
    private final static String KEY_MAC = "HmacSHA256";  // 根秘钥
  
    /** 
     * 全局数组 
     */  
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5","6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };  
  
    /** 
     * BASE64 解密 
     * @param key 需要解密的字符串 
     * @return 字节数组 
     * @throws Exception 
     */  
	private static  byte[] decryptBase64(String key) throws Exception {  
        return (new BASE64Decoder()).decodeBuffer(key);  
    }  
	
	/** 
     * BASE64 加密 
     * @param key 需要加密的字节数组 
     * @return 字符串 
     * @throws Exception 
     */  
	private static String encryptBase64(byte[] key) throws Exception {  
        return (new BASE64Encoder()).encodeBuffer(key);  
    }  
  
    /** 
     * 初始化HMAC密钥 
     * @return 
     */  
    public static String init() {  
        SecretKey key;  
        String str = "";  
        try {  
            KeyGenerator generator = KeyGenerator.getInstance(KEY_MAC);  
            key = generator.generateKey();  
            str = encryptBase64(key.getEncoded());  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return str;  
    }
  
    /** 
     * HMAC加密 
     * @param data 需要加密的字节数组 
     * @param key 密钥 
     * @return 字节数组 
     */  
    public static byte[] encryptHMAC(byte[] data, String key) {  
        SecretKey secretKey;  
        byte[] bytes = null;  
        try {  
//            secretKey = new SecretKeySpec(decryptBase64(key), key);  
            secretKey = new SecretKeySpec(decryptBase64(key), "HmacSHA256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
            mac.init(secretKey);  
            bytes = mac.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bytes;  
    }  
  
    /** 
     * HMAC加密 
     * @param data 需要加密的字符串 
     * @param key 密钥 
     * @return 字符串 
     */  
    public static String encryptHMAC(String data, String key) {  
        if (Strings.isNullOrEmpty(data)) {  
            return null;  
        }  
        byte[] bytes = encryptHMAC(data.getBytes(), key);  
        return byteArrayToHexString(bytes);  
    }  
  
  
    /** 
     * 将一个字节转化成十六进制形式的字符串 
     * @param b 字节数组 
     * @return 字符串 
     */  
    private static String byteToHexString(byte b) {  
        int ret = b;  
        //System.out.println("ret = " + ret);  
        if (ret < 0) {  
            ret += 256;  
        }  
        int m = ret / 16;  
        int n = ret % 16;  
        return hexDigits[m] + hexDigits[n];  
    }  
  
    /** 
     * 转换字节数组为十六进制字符串 
     * @param bytes 字节数组 
     * @return 十六进制字符串 
     */  
    private static String byteArrayToHexString(byte[] bytes) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < bytes.length; i++) {  
            sb.append(byteToHexString(bytes[i]));  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 测试方法 
     * @param args 
     */  
    public static void main(String[] args) throws Exception {  
    	 String key = HMAC.init();  
         System.out.println("Mac随机秘钥:\n" + key);
         String key2 = "1234";
         String word = "123";  
         System.out.println(encryptHMAC(word, key2));  
    } 
    
}
