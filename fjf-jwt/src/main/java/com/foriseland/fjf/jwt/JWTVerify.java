package com.foriseland.fjf.jwt;

import org.apache.commons.lang3.StringUtils;
import com.foriseland.fjf.encryption.HMAC;
import com.foriseland.fjf.util.XBase64;

/**
 * 
 * @className: JWTVerify 
 * @describe: verifyToken 验签,对Token的有效性进行验证
 * @author: wangHaiyang
 * <p>
 *  RETURN_500 = 500 token is null
    RETURN_501 = 501 token to array is null
    RETURN_502 = 502 token to array  less 3
    RETURN_503 = 503 token verify failure error
    RETURN_200 = 200 token verify success  
   </p>
 * @createTime 2017年12月20日 上午9:30:39
 */
public class JWTVerify {
	static String SPLIT = "\\.";

	private String[] signatureArrays(String token) {
		String[] signatureArrays = token.split(SPLIT);
		return signatureArrays;
	}
	
	
	/**
	 * 
	 * @title: verifyToken 
	 * @describe: 验签,对Token的有效性进行验证
	 * @param token
	 * @return {
	 *  RETURN_500 = 500 token is null
	 *  RETURN_501 = 501 token to array is null
	 *  RETURN_502 = 502 token to array  less 3
	 *  RETURN_503 = 503 token verify failure error
	 *  RETURN_200 = 200 token verify success
	 * }
	 * @author: wangHaiyang 
	 * @createTime 2017年12月20日 上午9:24:37 
	 * @throws
	 */
	public int verifyToken(String token) {
		if (StringUtils.isBlank(token)) {
			return Constants.RETURN_500;
		}
		String[] signatureArrays = signatureArrays(token);
		if (null == signatureArrays) {
			return Constants.RETURN_501;
		}
		int arrayLength = signatureArrays.length;
		if (arrayLength < 3) {
			return Constants.RETURN_502;
		}
		String head = signatureArrays[0];
		String payload = signatureArrays[1];
		String hmacSignature = signatureArrays[2];
		String hmacEncode = encryptSignature(head, payload);
		if (hmacEncode.equals(hmacSignature)) {
			return Constants.RETURN_200;
		}
		return Constants.RETURN_503;
	}

	private  String encryptSignature(String head, String payload) {
		String decodeBase64Head = XBase64.decodeBase64(head);
		String decodeBase64Payload = XBase64.decodeBase64(payload);
		StringBuilder process = new StringBuilder();
		process.append(XBase64.encodeBase64(decodeBase64Head)).append(".")
				.append(XBase64.encodeBase64(decodeBase64Payload));
		String encryptSignature = HMAC.encryptHMAC(process.toString(), Constants.KEY_MAC); // 根秘钥 @TODO 临时放在这后期读取signature.so文件
		return encryptSignature;
	}

	public static void main(String[] args) {
		// eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHBUaW1lIjoxNTEzNjc5OTkwMTMxLCJpYXRUaW1lIjoxNTEzNjc5OTkwMzUzLCJtaWQiOiIzYWQwOGI1ZTVlYWY0OGNhOTk4OTVjOTJjNTE2NzUyNCIsImlzc3VlciI6Ind3dy5mb3Jpc2VsYW5kLmNvbSJ9
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3N1ZXIiOiJ3d3cuZm9yaXNlbGFuZC5jb20iLCJleHBUaW1lIjoxNTEzNzYwMjU5MzcxLCJtaWQiOiI5NWZkMDgwODY3ZmI0ODZlOWEzMTRmYWIwOThjZjQzYyIsImlhdFRpbWUiOjE1MTM3NjAyNTk0Mjl9.372dd45b88c23f9b3d0262144ba45b64c7ae0cf1740fbfa93d60673f04fda04f";
		JWTVerify bean = new JWTVerify();
		int verifySignature = bean.verifyToken(token);
		System.out.println(verifySignature);
		
		String len = "372dd45b88c23f9b3d0262144ba45b64c7ae0cf1740fbfa93d60673f04fda04f";
		System.out.println(len.length());
	}

}
