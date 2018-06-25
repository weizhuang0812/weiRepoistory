package com.foriseland.fjf.encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

public class AES256 {

	/**
	 * 加密
	 * @param value  需要加密的内容
	 * @param salt 秘钥
	 * @return
	 * @throws Exception 
	 */
	public static String encrypt(String value, String salt) throws Exception {
		try {
			if(StringUtils.isBlank(value)) { // 参数为空
				throw new RuntimeException("AES256 encrypt error,value is null");
			}
			if(StringUtils.isBlank(salt)) { // 参数为空
				throw new RuntimeException("AES256 encrypt error,salt is null");
			}
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			//防止linux下 随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );   
            secureRandom.setSeed(salt.getBytes());   
            kgen.init(128, secureRandom);
			//kgen.init(128, new SecureRandom(salt.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = value.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return parseByte2HexStr(result); // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param value  待解密内容
	 * @param salt 解密密钥
	 * @return
	 * @throws Exception 
	 */
	public static String decrypt(String value, String salt) throws RuntimeException {
		try {
			if(StringUtils.isBlank(value)) { // 参数为空
				throw new RuntimeException("AES256 decrypt error,value is null");
			}
			if(StringUtils.isBlank(salt)) { // 参数为空
				throw new RuntimeException("AES256 decrypt error,salt is null");
			}
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			//防止linux下 随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );   
            secureRandom.setSeed(salt.getBytes());   
            kgen.init(128, secureRandom);
			//kgen.init(128, new SecureRandom(salt.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(parseHexStr2Byte(value));
			return new String(result); // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {

		String content = "123";
		String password = "zsyy";
		System.out.println("明文：" + content);
		System.out.println("key：" + password);

		String encryptResult = AES256.encrypt(content, password);
		System.out.println("密文：" + encryptResult);

		String decryptResult = AES256.decrypt(encryptResult, password);
		System.out.println("解密：" + new String(decryptResult));
	}
}
