package com.foriseland.fjf.encryption.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptUtil {
	final static String KEY_ALGORITHM = "AES";
	// 加解密算法/模式/填充方式
	final static String algorithmStr = "AES/CBC/PKCS7Padding";
	//
	private static Key key;
	private static Cipher cipher;
	boolean isInited = false;

	/**
	 * 与商城是数据传输的加密
	 * 
	 * @param str
	 * @param keys
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static String aesEncrypt(String str, String keys, String iv) throws Exception {
		String result = "";
		if (StringUtils.isBlank(str) || StringUtils.isBlank(keys) || StringUtils.isBlank(keys))
			return null;
		byte[] encryptedText = null;
		init(keys.getBytes());
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
			encryptedText = cipher.doFinal(str.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String bytesToHexString = BytesToHex.bytesToHexString(encryptedText);
		int i = (int) (Math.random() * 900) + 100;
		result += i;
		result += bytesToHexString;
		result.replaceAll("-", "");
		return result;
	}

	/**
	 * 解密
	 * 
	 * @param str
	 * @param keys
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static String aesDecrypt(String str, String keys, String iv) throws Exception {
		if (StringUtils.isBlank(str) || StringUtils.isBlank(keys))
			return null;
		byte[] encryptedText = null;
		init(keys.getBytes());
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
			encryptedText = cipher.doFinal(BytesToHex.hexStringToBytes(str));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(encryptedText, "utf-8");
	}

	/**
	 * 加密
	 * 
	 * @param str
	 * @param keys
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static String aesEncryptProperties(String str, String keys, String iv) throws Exception {
		if (StringUtils.isBlank(str) || StringUtils.isBlank(keys) || StringUtils.isBlank(iv))
			return null;
		byte[] encryptedText = null;
		init(keys.getBytes());
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
			encryptedText = cipher.doFinal(str.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return BytesToHex.bytesToHexString(encryptedText);
	}

	public static void init(byte[] keyBytes) {

		// 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
		int base = 16;
		if (keyBytes.length % base != 0) {
			int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
			byte[] temp = new byte[groups * base];
			Arrays.fill(temp, (byte) 0);
			System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
			keyBytes = temp;
		}
		// 初始化
		Security.addProvider(new BouncyCastleProvider());
		// 转化成JAVA的密钥格式
		key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
		try {
			// 初始化cipher
			cipher = Cipher.getInstance(algorithmStr, "BC");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
