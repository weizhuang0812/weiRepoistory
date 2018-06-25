package com.foriseland.fjf.lang;

import java.math.BigInteger;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Administrator 2014/12/16 10:52
 * @description !TODO描述
 */
public class FLNumberUtil extends NumberUtils {
	public static int compare(Number a, Number b) {
		a = a == null ? LONG_ZERO : a;
		b = b == null ? LONG_ZERO : b;
		return compare2(a.longValue(), b.longValue());
	}

	public static Short int2Short(Integer i) {
		return i == null ? null : toShort(FLStringUtil.toString(i));
	}

	public static Short int2ShortWithDef(Integer i, String def) {
		return toShort(FLStringUtil.toString(i, def));
	}

	private static int compare2(long lhs, long rhs) {
		if (lhs < rhs) {
			return -1;
		}
		if (lhs > rhs) {
			return +1;
		}
		// Need to compare bits to handle 0.0 == -0.0 being true
		// compare should put -0.0 < +0.0
		// Two NaNs are also == for compare purposes
		// where NaN == NaN is false
		int lhsBits = Float.floatToIntBits(lhs);
		int rhsBits = Float.floatToIntBits(rhs);
		if (lhsBits == rhsBits) {
			return 0;
		}
		// Something exotic! A comparison to NaN or 0.0 vs -0.0
		// Fortunately NaN's int is > than everything else
		// Also negzeros bits < poszero
		// NAN: 2143289344
		// MAX: 2139095039
		// NEGZERO: -2147483648
		if (lhsBits < rhsBits) {
			return -1;
		} else {
			return +1;
		}
	}

	public static long batchSize(long a, long b) {
		return a / b + (a % b > 0 ? 1 : 0);
	}

	public static boolean notEquals(Number a, Number b) {
		return compare(a, b) != 0;
	}

	public static boolean equals(Number a, Number b) {
		return compare(a, b) == 0;
	}

	public static boolean bigger(Number a, Number b) {
		if (a == null || b == null) {
			return false;
		}
		return compare(a, b) > 0;
	}
	
	/**
	 * >=
	 * @author Bigy
	 * @date   2015年4月16日
	 * @desc   
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean ebigger(Number a, Number b) {
		if (a == null || b == null) {
			return false;
		}
		return compare(a, b) >= 0;
	}

	public static boolean between(Number value, Number min, Number max) {
		if (min == null || max == null || value == null) {
			return false;
		}
		return bigger(value, min) && less(value, max);
	}

	public static boolean less(Number a, Number b) {
		if (a == null || b == null) {
			return false;
		}
		return compare(a, b) < 0;
	}
	
	/**
	 * <=
	 * @author Bigy
	 * @date   2015年4月16日
	 * @desc   
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean eless(Number a, Number b) {
		if (a == null || b == null) {
			return false;
		}
		return compare(a, b) <= 0;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 22; i++) {
			System.out.println((i & (2 ^ 5 - 1)) + "_" + i + "&11_|_" + i + "%11_" + (i % 2 ^ 5));
			System.out.println();
		}
	}

	public static int binary2Int(String str) {
		int i = 0;
		if (FLStringUtil.isNotBlank(str)) {
			BigInteger bi = new BigInteger(str, 2);
			i = bi.intValue();
		}
		return i;
	}

	public static String int2Binary(String i) {
		String b = "";
		if (FLStringUtil.isNotBlank(i) && FLNumberUtil.isDigits(i)) {
			BigInteger bi = new BigInteger(i);
			b = bi.toString(2);
		}
		return b;
	}

	public static Integer toInteger(Object obj) {
		if(obj == null){
			return null;
		}
		return toInt(FLStringUtil.toString(obj));
	}

	public static int toInt(Integer i) {
		return toInt(FLStringUtil.toString(i));
	}

	public static int toInt(Object i) {
		return toInt(FLStringUtil.toString(i));
	}

	public static int toInt(String i) {
		return toInt(FLStringUtil.toString(i));
	}

	public static Long toLong(Object obj) {
		return toLong(FLStringUtil.toString(obj));
	}

	public static Long toLong(Object obj, Long def) {
		return toLong(FLStringUtil.toString(obj), def);
	}

	public static boolean toBoolean(Object object) {
		return compare(toInt(object), 0) > 0;
	}

	public static boolean exist(Set<? extends Number> sets, Number obj) {
		if(sets == null){
			return false;
		}
		for (Number num : sets) {
			if (FLNumberUtil.equals(num, obj)) {
				return true;
			}
		}
		return false;
	}

}
