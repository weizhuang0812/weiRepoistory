package com.foriseland.fjf.jwt;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.foriseland.fjf.encryption.HMAC;
import com.foriseland.fjf.util.XBase64;
import com.foriseland.fjf.uuid.ShortUUIDGenerator;
import com.google.common.collect.Maps;

/**
 * @className: JWTProcessing 
 * @describe: 基于Token的身份验证是无状态的，我们不将用户信息存在服务器或Session中。相比原始的Cookie+Session方式，更适合分布式系统的用户认证，绕开了传统的分布式Session一致性等问题
 * <blockquote><pre>
 * 相比Cookie认证的优势
 * </pre></blockquote>
 * <p>
 * 支持跨域跨站点访问:
 *  Cookie是不允许垮域访问的，可以通过设置顶级域名的方式实现部分跨域，但是跨站点的访问仍然不支持，
 * 如果使用Token机制，就可以通过HTTP头传输用户认证信息，从而更好的实现跨域跨站点。
 * </p>
 * <p>
 * 无状态:
 * Token机制在服务端不需要存储session信息，Token自身包含了登录用户的信息，只需要在客户端的cookie或本地介质存储状态信息；
 * 去耦:不需要绑定到一个特定的身份验证方案。Token可以在任何地方生成，只要在你的API被调用的时候，你可以进行Token生成调用即可；
 * </p>
 * <p>
 * 更适用于移动应用:
 * 当客户端是原生应用时，Cookie是不被支持的，虽然目前Webview的方式可以解决Cookie问题，
 * 但是显然采用Token认证机制会简单得多；
 * </p>
 * <p>
 * 安全性更强:
 * 因为不再依赖于Cookie，所以你就不需要考虑对CSRF（跨站请求伪造）的防范；
 * </p>
 * <p>
 * 标准化易扩展:
 * 可以采用标准化的 JSON Web Token (JWT)，对以后系统接入Node等纯前端开发更便捷；
 * </p>
 * <p>
 * 相比Session一致性提高性能:
 * 相比服务端保存Session一致性信息，并查询用户登录状态，一般来说Token的验证过程（包含加密和解密），性能开销会更小。
 * </p>
 * @author: wangHaiyang 
 * @createTime 2017年12月14日 下午1:57:28
 */
/*
 * Payload 里面是 Token 的具体内容，这部分内容可以自定义，JWT有标准字段，也可以添加其它需要的内容。
 * header.payload.signature
 *  { "iss": "www.foriseland.com", "exp": "1470730182", "uid": "12345abcde" }
 *  发行者（网站）、过期时间和用户id
 */
public class JWTProcessing {

	private String issuer; // iss：Issuer，发行者
	private long expTime; // Expiration time，过期时间
	private String mid; // 用户的uid唯一标识 | mid
	
	private Long iatTime = System.currentTimeMillis();
	
	public JWTProcessing (String issuer,long expTime,String mid) {
		this.issuer = issuer;
		this.expTime = expTime;
		this.mid = mid;
	}
	
	public Field[] declaredFields(){
		Field[] fields = this.getClass().getDeclaredFields();
		return fields;
	}
	
	
	public String issuer(){ //#Issuer，发行者
		return Thread.currentThread().getStackTrace()[1].getMethodName();
	}
	
	public String expTime(){ // #token过期时间
		return Thread.currentThread().getStackTrace()[1].getMethodName();
	}
	
	public String mid(){ // #用户mid
		return Thread.currentThread().getStackTrace()[1].getMethodName();
	}
	
	public String iatTime(){ // #token发布时间
		return Thread.currentThread().getStackTrace()[1].getMethodName();
	}
	
	public String getIssuer() {
		return issuer;
	}

	public long getExpTime() {
		return expTime;
	}

	public String getMid() {
		return mid;
	}

	
	private String mapToJson(HashMap<String,Object> soltMap){
		return JSON.toJSONString(soltMap);
	}

	static final String JWT_KEY_TYPE = "typ";
	static final String JWT_KEY_ALG = "alg";
	static final String JWT_TYPE = "JWT";
	static final String JWT_ALG = "HS256";
	
	/**
	 * @title: base64Head 
	 * @describe:生成Base64的base64Head | alg描述的是签名算法
	 * @return XBase64.encodeBase64
	 * @author: wangHaiyang 
	 * @createTime 2017年12月14日 下午6:11:14 
	 * @throws
	 */
	public String base64Head() {
		HashMap<String,Object> headMap = Maps.newHashMap();
		headMap.put(JWT_KEY_TYPE, JWT_TYPE);
		headMap.put(JWT_KEY_ALG, JWT_ALG);
		return XBase64.encodeBase64(this.mapToJson(headMap));
	}
	
	private String payloadToJson(){
		HashMap<String,Object> payloadMap = Maps.newLinkedHashMap();
		payloadMap.put(this.issuer(), this.issuer);
		payloadMap.put(this.expTime(), this.expTime);
		payloadMap.put(this.mid(), this.mid);
		payloadMap.put(this.iatTime(), this.iatTime);
		return this.mapToJson(payloadMap);
	}
	
	/**
	 * @title: base64Payload 
	 * @describe:生成Base64的base64Payload | 载荷部分
	 * @return XBase64.encodeBase64
	 * @author: wangHaiyang 
	 * @createTime 2017年12月14日 下午6:11:14 
	 * @throws
	 */
	public String base64Payload() {
		String payloadJson = this.payloadToJson();
		String base64 = XBase64.encodeBase64(payloadJson);
		return base64;
	}

	/**
	 * @title: signature 
	 * @describe:生成Base64的signature | 签名的目的是用来验证头部和载荷是否被非法篡改
	 * step1:首先将Base64编码后的base64Head和base64Signature用.连接在一起
	 * step2:对这个字符串使用HmacSHA256算法进行加密，这个密钥secret存储在服务端，前端不可见
	 * step3:然后将Signature和前面两部分拼接起来，得到最后的token
	 * @return XBase64.encodeBase64
	 * @author: wangHaiyang 
	 * @createTime 2017年12月14日 下午6:11:14 
	 * @throws
	 */
	private String base64Signature() {
		StringBuilder process = new StringBuilder();
		process.append(this.base64Head()).append(".").append(this.base64Payload());
		return process.toString();
	}
	
	/**
	 * @title: signature 
	 * @describe: HMACSHA256(Base64(base64Head) + "." + Base64(base64Payload),  secret)
	 * <p>签名的目的是用来验证头部和载荷是否被非法篡改;</p>
	 * <p>验签过程描述：读取Header部分并Base64解码，得到签名算法。根据以上方法算出签名，如果签名信息不一致，说明是非法的。</p>
	 * <p>然后将Signature和前面两部分拼接起来，得到最后的token：
	 *   ewogICJ0eXAiOiAiSldUIiwKICAiYWxnIjogIkhTMjU2Igp9.ewogImlzcyI6ICJjaGJsb2dzLmNvbSIsCiAiZXhwIjogIjE0NzA3MzAxODIiLAogInVpZCI6ICIxMjM0NWFiY2RlIiwKfQ.9q2eq8sa374ao2uq9607r6qu6
	 * </p>
	 * @return HMAC.encryptHMAC(this.base64Signature(), saltKey);
	 * @author: wangHaiyang 
	 * @createTime 2017年12月14日 下午6:35:50 
	 * @throws
	 */
	public String encryptSignature(){
		String base64Signature = this.base64Signature();
		String encryptSignature = HMAC.encryptHMAC(base64Signature, Constants.KEY_MAC); // 根秘钥 @TODO 临时放在这后期读取signature.so文件
		return base64Signature+"."+encryptSignature;
	}

	public static void main(String[] args) {
		String issuer = "www.foriseland.com"; // iss：Issuer，发行者
		long expTime = System.currentTimeMillis(); // Expiration time，过期时间
		String uid = ShortUUIDGenerator.get32UUID(); //
		JWTProcessing bean = new JWTProcessing(issuer,expTime,uid);
		String res = bean.encryptSignature();
		System.out.println(res);
		//XBase64.decodeBase64(binaryData)
//		String[]signatureArrays = res.split("\\.");
//		String head = signatureArrays[0];
//		String payload = signatureArrays[1];
//		String hmacSignature = signatureArrays[2];
		
//		System.out.println("head:"+head);
//		System.out.println("payload:"+payload);
//		String s1 = bean.encryptSignature(head,payload);
//		System.out.println(res);
		
	}
}
