package com.foriseland.fjf.lang;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * @author Administrator 2014/12/16 10:51
 * @description !TODO描述
 */
public class FLStringUtil extends StringUtils {
    private final static Logger log = LoggerFactory.getLogger(FLStringUtil.class);

    public static String toString(Object obj) {
        return toString(obj, null);
    }

    public static boolean notEquals(CharSequence cs1, CharSequence cs2) {
        return !equals(cs1, cs2);
    }

    public static String toString(Object obj, String def) {
        return isNotNull(obj) ? String.valueOf(obj) : def;
    }

    public static String toString2Utf8(Object obj, String def) throws UnsupportedEncodingException {
        return isNotNull(obj) ? trim(new String(toString(obj).getBytes(), "UTF-8")) : def;
    }

    public static String toString2Utf8(Object obj) throws UnsupportedEncodingException {
        return toString2Utf8(obj, "");
    }

    public static String toString2Trim(Object obj) {
        return trim(obj);
    }

    public static String trim(Object obj) {
        return trim(toString(obj, null));
    }

    public static String trim(String str) {
        return isNull(str) ? null : toString(str, "").trim();
    }

    public static boolean isNotBlank(Object obj) {
        return isNotBlank(toString(obj));
    }

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isBlank(Object obj) {
        return isBlank(toString(obj));
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 != null) {
            return trim(obj1).equals(trim(obj2));
        } else if (obj2 != null) {
            return trim(obj2).equals(trim(obj1));
        } else {
            return true;
        }
    }

    public static String replaceAll(String permissions, String regex, String replacement) {
        if (isNotBlank(permissions)) {
            permissions = permissions.replaceAll(regex, replacement);
        }
        return permissions;
    }

    public static String toInSql(List<Long> lsIds) {
        if (lsIds != null) {
            return Joiner.on(",").join(lsIds);
        }
        return "";
    }

    public static String toInSql(Set<Long> setIds) {
        if (setIds != null) {
            return Joiner.on(",").join(setIds);
        }
        return "";
    }

    public static String toInSql(List<Long> lsIds, boolean withScale) {
        if (lsIds != null) {
            if (withScale) {
                return "(" + toInSql(lsIds) + ")";
            } else {
                return toInSql(lsIds);
            }
        }
        return "";
    }

    public static Set<Long> ids2Set(String ids) {
        Set<Long> setIds = Sets.newHashSet();
        if (FLStringUtil.isNotBlank(ids)) {
            String[] arrayIds = ids.split(",");
            for (String id : arrayIds) {
                if (FLStringUtil.isBlank(id)) {
                    continue;
                }
                if (FLStringUtil.contains(id, ":")) {
                    id = FLStringUtil.split(id, ":")[0];
                }
                long lId = FLNumberUtil.toLong(id);
                if (lId > 0) {

                    setIds.add(lId);
                }
            }
        }
        return setIds;
    }

    public static String set2IdsSikpNull(Set<Long> setIds, String regx, boolean wrap) {
        if (setIds == null) {
            return "";
        }
        StringBuilder sbu = new StringBuilder(setIds.size());
        Iterator<Long> itl = setIds.iterator();
        int i = 0;
        while (itl.hasNext()) {
            Long l = itl.next();
            if (FLNumberUtil.equals(l, 0)) {
                log.info("set2IdsSikpNull skip " + l);
                continue;
            }
            if (i == 0) {
                sbu.append(l);
                i++;
            } else {
                sbu.append(regx).append(l);
            }
        }
        return sbu.toString();
    }

    public static String set2StringSikpNull(Set<String> setIds, String regx, boolean wrap) {
        if (setIds == null) {
            return "";
        }
        StringBuilder sbu = new StringBuilder(setIds.size());
        Iterator<String> itl = setIds.iterator();
        int i = 0;
        while (itl.hasNext()) {
            String str = itl.next();
            if (FLStringUtil.isBlank(str)) {
                log.info("set2IdsSikpNull skip " + str);
                continue;
            }
            if (i == 0) {
                sbu.append(str);
                i++;
            } else {
                sbu.append(regx).append(str);
            }
        }
        if (wrap) {
            sbu.append(regx).insert(0, regx);
        }
        return sbu.toString();
    }

    public static String set2StringSikpNull(Set<String> setIds, String regx) {
        return set2StringSikpNull(setIds, regx, false);
    }
    
	/**
	 * 检测是否为手机号码
	 * 
	 * @Title: checkTelephone
	 * @Description: TODO
	 * @param @param tel
	 * @param @return
	 * @return boolean
	 * @throws
	 * @Date 2014年3月20日 下午2:46:47
	 */
	public static boolean isMobile(String tel) {
		if(isBlank(tel)){
			return false;
		}
		String regExp = "^((1[3-9][0-9]))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(trim(tel));
		return m.find();
	}

	/**
	 * 计算两个字符串最长相同子串
	 * 
	 * @Title: search
	 * @Description: TODO
	 * @param @param s1
	 * @param @param s2
	 * @param @return
	 * @return String
	 * @throws
	 * @Date 2014年3月17日 下午1:44:33
	 */
	public static String search(String s1, String s2) {
		String max = "";
		for (int i = 0; i < s1.length(); i++) {
			for (int j = i; j <= s1.length(); j++) {
				String sub = s1.substring(i, j);
				// System.out.println(sub);
				if ((s2.indexOf(sub) != -1) && sub.length() > max.length()) {
					max = sub;
				}
			}
		}
		return max;
	}

    /**
     * 验证是否为空
     * 
     * @param value
     * @return true null or '' or ' ' false 'a'
     */
    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0
                || value.toString().trim().length() == 0;
    }

    /**
     * 验证是否不为空
     * 
     * @param value
     * @return true 'a' false null or '' or ' '
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
    
    /**
	 * 生成随机数
	 */
	public static String generateRandomInt2String(int length) {
		String seed = "0123456789";
		int subIndex = 0;
		String retString = "";
		for (int i = 0; i < length; i++) {
			subIndex = (int) (Math.random() * 100 % seed.length());
			retString += seed.substring(subIndex, subIndex + 1);
		}
		return retString;
	}
	
	/**
	 * 生成二维码id
	 * @return 格式 yyyyMMddHHmmssSSS + XXX(随机数）
	 */
	public static String generateQrcode() {
		String now = new DateTime().toString("yyyyMMddHHmmssSSS");
		String qrcodesId = new StringBuilder(String.valueOf(now)).append(generateRandomInt2String(3)).toString();
		return qrcodesId;
	}
	
	/**
	 * 生成随机数字
	 * 
	 * @param length
	 * @return
	 */
	public static String randomNumber(int length) {
		char[] numbersAndLetters = null;
		java.util.Random randGen = null;
		if (length < 1) {
			return null;
		}
		// Init of pseudo random number generator.
		if (randGen == null) {
			if (randGen == null) {
				randGen = new java.util.Random();
				// Also initialize the numbersAndLetters array
				numbersAndLetters = ("0123456789").toCharArray();
			}
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(9)];
		}
		return new String(randBuffer);
	}
	
	/**
     * Gets the random string.
     * 
     * @param randomNumberSize
     *            the random number size
     * @param ipAddress
     *            the ip address
     * @param port
     *            the port
     * @return the random string
     */
    public static String getRandomString(int randomNumberSize,
            String ipAddress, int port) {
        long number = 0;
        number += ipToLong(ipAddress);
        number += port;
        Random randomGen = new Random();
        number += randomGen.nextLong();

        /**
         * 取mod
         */
        String defaultString = getZeroString(randomNumberSize);
        StringBuilder modStringBuilder = new StringBuilder();
        modStringBuilder.append("1").append(defaultString);
        long mod = Long.parseLong(modStringBuilder.toString());

        /**
         * 算随机值
         */
        number = number > 0 ? number % mod : Math.abs(number) % mod;

        /**
         * 格式化返回值 为randomNumberSize位
         */
        DecimalFormat df = new DecimalFormat(defaultString);
        return df.format(number);
    }
    
    /**
     * Ip to long.
     * 
     * @param ipAddress
     *            the ip address
     * @return the long
     */
    public static long ipToLong(String ipAddress) {
        long result = 0;
        String[] atoms = ipAddress.split("\\.");

        for (int i = atoms.length - 1, j = 0; i >= j; i--) {
            result |= (Long.parseLong(atoms[atoms.length - 1 - i]) << (i * 8));
        }

        return result & 0xFFFFFFFF;
    }
    
    /**
     * Gets the zero string.
     * 
     * @param length
     *            the length
     * @return the zero string
     */
    public static String getZeroString(int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append("0");
        }
        return buffer.toString();
    }
    
    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String randomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
	
}
