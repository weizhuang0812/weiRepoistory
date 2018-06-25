package com.foriseland.fjf.uuid;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.foriseland.fjf.lang.FLStringUtil;
import com.github.zkclient.NetworkUtil;


/**
 * 序号生成器框架实现，多线程安全
 * 调用getNo()即可得到生成的序号
 *  * 生成订单号（25位），格式：2位业态码+15位时间戳+4位序号+3位随机数+1版本号
 *  * 生成订单号（25位），格式：2位业态码+15位时间戳+4位序号+3位（服务器主机名后面几位）+1版本号
 *    生成流水号（26位），格式：2位业态码+1位固定标示+15位时间戳+4位序号+3位随机数+1版本号
 *   第二版本
 *  *  * 生成订单号（25位），格式：2位业态码+3位（服务器主机名后面几位）+4位序号+15位时间戳+1版本号
 *                                                      NG            038                                            0001      160627170936189 2
 *    生成流水号（26位），格式：2位业态码+1位固定标示+3位服务器主机名后面几位+4位序号+15位时间戳+1版本号
 *
 */
public abstract class AbstractSequence {

    /** The current number. */
    private long currentNumber;

    /** The last time. */
    private String lastTime;

    /**
     * Gets the no.
     *  这个方法会有并发问题，谨慎使用
     * @return the no
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public synchronized String getNo() throws Exception {
        String currentTime = getCurrentTime();
        if (currentTime.equals(lastTime)) {
            currentNumber++;
        } else {
            // 时间改变，回到起点
            currentNumber = 1;
            lastTime = currentTime;
        }
        return new StringBuffer().append(getFirstPart())
                .append(getSecondPart()).append(getThridPart())
                .append(getForthPart()).append(getFifthPart()).toString();
    }


    public synchronized String getNo(String bizTypeNo) throws Exception {
        String currentTime = getCurrentTime();
        if (currentTime.equals(lastTime)) {
            currentNumber++;
        } else {
            // 时间改变，回到起点
            currentNumber = 1;
            lastTime = currentTime;
        }
        return new StringBuffer().append(getFirstPart(bizTypeNo))
                .append(getSecondPart()).append(getThridPart())
                .append(getForthPart()).append(getFifthPart()).toString();
    }

    protected String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getDateFormat());
        return dateFormat.format(new Date());
    }

    /**
     * Gets the first part.
     * 
     * @return the first part
     */
    public String getForthPart() {
        return getEncryptString(lastTime);
    }

    /**
     * Gets the thrid part.
     * 
     * @return the thrid part
     */
    public String getThridPart() {
        String currentNumberString = String.valueOf(currentNumber);
        if (getThirdPartLength() < currentNumberString.length()) {
            throw new RuntimeException(String.format("Number %s is overflow!!",
                    currentNumber));
        }
        return new DecimalFormat(
        		FLStringUtil.getZeroString(getThirdPartLength()))
                .format(currentNumber);
    }

    /**
     * Gets the thrid part.
     * 
     * @return the thrid part
     */
    public String getSecondPart() {
        //默认为3位随机数
        String forthPart = FLStringUtil.randomNumber(3);

        //如果可以取到符合格式的主机名，用主机的IP部分
        String hostname = NetworkUtil.getLocalhostName();
        if(StringUtils.isNotEmpty(hostname)){
            int lastIndex = hostname.lastIndexOf("-");
            if(lastIndex > 0) {
                hostname = hostname.substring(lastIndex+1);
                if (StringUtils.isNotEmpty(hostname)) {
                    forthPart = hostname;
                }
            }
        }

        forthPart =   String.format("%03d", NumberUtils.toInt(forthPart, 0));
        if("000".equals(forthPart)){
            forthPart = FLStringUtil.randomNumber(3);
        }
        return forthPart;
    }
    
    /**
     *  这个方法会有并发问题，由于bizTypeNo是全局静态变量，在并发情况下可能会出现混的情况
     * @return 2位
     */
    @Deprecated
    public String getFirstPart(){
    	return getBizTypeNo();
    }

    public abstract String getFirstPart( String bizTypeNo);

    /**
     * 
     * @return 1位
     */
    public String getFifthPart(){
    	return getVersionNo();
    }
    /**
     * Gets the encrypt string.
     * 
     * @param arg
     *            the arg
     * @return the encrypt string
     */
    public String getEncryptString(String arg) {
        return arg;
    }

    /**
     * Gets the date format.
     * 
     * @return the date format
     */
    public abstract String getDateFormat();

    /**
     * Gets the third part length.
     * 
     * @return the third part length
     */
    public abstract int getThirdPartLength();
    /**
     * 
     * @return 业态码2位数
     */
    public abstract String getBizTypeNo();
    /**
     * 
     * @return 版本号1位
     */
    public abstract String getVersionNo();
    
    
}
