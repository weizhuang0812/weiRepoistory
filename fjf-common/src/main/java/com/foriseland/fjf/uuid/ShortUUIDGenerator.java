package com.foriseland.fjf.uuid;

import java.util.Random;
import java.util.UUID;

import com.foriseland.fjf.lang.HashUtil;

/**
 * @className: ShortUUIDGenerator 
 * @describe: 生成短uuid
 * @author: PC  
 * @createTime 2017年12月8日 上午9:56:15
 */
public final class ShortUUIDGenerator {

    private ShortUUIDGenerator(){}


    private static Random RANDOM = new Random();


    private static final char[] NUMBER = new char[]{
            '0','1','2','3','4','5','6','7','8','9'
    };

    public synchronized static long generator(){
        return HashUtil.hash32(String.valueOf(System.currentTimeMillis()+getRandom()));
    }


    private static int getRandom(){
        char [] randBuffer = new char[3];
        for (int i=0; i<randBuffer.length; i++) {
            randBuffer[i] = NUMBER[RANDOM.nextInt(10)];
        }
        return Integer.parseInt(String.valueOf(randBuffer));
    }
    
    /**
     * 获取32位UUID
     * @return
     */
    public static String get32UUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
