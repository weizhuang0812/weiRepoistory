package com.foriseland.fjf.util;

import io.netty.util.internal.ThreadLocalRandom;

public class RandomNum {

//	private long upper = 100;
//	private long lower = 0;
//	private String algorithm = "SHA1PRNG";
//	private String provider = "SUN";
//	private SecureRandom secrandom = null;
//
//	public static RandomNum random() throws Exception {
//		return new RandomNum();
//	}
//
//	private RandomNum() throws Exception {
//		secrandom = SecureRandom.getInstance(algorithm, provider);
//	}
//
//	private final float getFloat() {
//		if (secrandom == null)
//			return secrandom.nextFloat();
//		else
//			return secrandom.nextFloat();
//	}
//
//	/**
//	 * generate the random number
//	 *
//	 */
//	private final Number getRandom(long lower,long upper) {
//		long randomnum = new Long(lower + (long) ((getFloat() * (upper - lower))));
//		return randomnum;
//	}
//	
//	public final Number getSixBit() {
//		return getRandom(100000,999999);
//	}
//	
//	public final Number getFourBit() {
//		return getRandom(1000,9999);
//	}
	
	/**
	 * ThreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。ThreadLocalRandom不是直接用new实例化，而是第一次使用其静态方法current()。
	 * 从Math.random()改变到ThreadLocalRandom有如下好处：我们不再有从多个线程访问同一个随机数生成器实例的争夺。取代以前每个随机变量实例化一个随机数生成器实例，我们可以每个线程实例化一个。\
	 * Java 7之前我们使用Math.random()产生随机数，使用原子变量来保存当前的种子，这样两个线程同时调用序列时得到的是伪随机数，而不是相同数量的两倍。
	 * @title: getSixBit | getFourBit
	 * @author: wangHaiyang 
	 * @createTime 2018年1月3日 下午6:53:04 
	 * @throws
	 */
	public static final int getSixBit(){
		return ThreadLocalRandom.current().nextInt(100000,999999);
	}
	
	public static final int getFourBit(){
		return ThreadLocalRandom.current().nextInt(1000,9999);
	}
}