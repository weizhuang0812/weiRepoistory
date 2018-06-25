package com.foriseland.fjf.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foriseland.fjf.constants.Constants;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author Administrator
 * @ Created by Administrator on 2014/11/24.
 */
public class PinYinUtil {
    private static final Logger log = LoggerFactory.getLogger(PinYinUtil.class);

    private static CacheLoader<String, String> checkLoader = new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
            Map<String, String> result = Resources.readLines(PinYinUtil.class.getClassLoader().getResource("pinyin.properties"),
                    Charsets.UTF_8, new LineProcessor<Map<String, String>>() {
                        Map<String, String> result = Maps.newHashMap();

                        public boolean processLine(String line) throws IOException {
                            if (Strings.isNullOrEmpty(line)) return true;
                            Iterable<String> iter = Splitter.on(Constants.EQUAL).omitEmptyStrings().trimResults().limit(2).split(line);
                            String[] arr = Iterables.toArray(iter, String.class);
                            if (arr.length == 2) {
                                result.put(arr[0], arr[1]);
                            }
                            return true;
                        }

                        public Map<String, String> getResult() {
                            return result;
                        }

                    });
            return result.get(key);
        }
    };

    private static LoadingCache<String, String> checkCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(checkLoader);

    public static String getPinYin(String src) {
        try {
            return checkCache.get(src);
        } catch (Exception ex) {
            log.warn("pinyin properties file do not have this chinese");
        }

        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                if (Character.toString(t1[i]).matches("[\\一-\\龥]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
                } else {
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination ex) {
            log.error("get string pinyin occurs exception!", ex);
        }
        return t4;
    }

    public static String getPinYinHeadChar(String str) {
        try {
            return checkCache.get(str);
        } catch (Exception ex) {
            log.warn("pinyin properties file do not have this chinese");
        }

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    public static String getUpperCaseFirstChar(String s) {
        if (StringUtils.isNotBlank(s)) {
            return getPinYinHeadChar(s).substring(0, 1).toUpperCase();
        }
        return "";
    }

    public static String getQueryKey(String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            if (StringUtils.isNotBlank(s))
                builder.append(s).append(getPinYin(s)).append(getPinYinHeadChar(s));
        }
        return builder.toString();
    }

    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

    public static void main(String[] args) {
        String value = getPinYin("重庆");
        System.out.println(value);
    }
}
