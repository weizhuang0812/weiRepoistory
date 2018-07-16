package com.foriseland.fif.telnet.vo;

/**
 * <p>ClassName: DruidResult</p>
 * <p>Description: </p>
 * <p>Company: 梦想工厂--基础架构部/p>
 *
 * @author weizhuang
 * @date 2018-07-16
 */
public class DruidResult {

    /**
     * 当前活动连接数
     */
    private Integer activeCount;
    /**
     * 最大活动连接数
     */
    private Integer maxActive;
    /**
     * 初始活动连接数
     */
    private Integer initialSize;


    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }


    @Override
    public String toString() {
        return "{ activeCount : "+activeCount+" maxActive : "+maxActive+" initialSize : "+initialSize+" }";
    }
}
