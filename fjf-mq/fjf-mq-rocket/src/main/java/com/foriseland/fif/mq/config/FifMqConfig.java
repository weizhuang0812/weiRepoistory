package com.foriseland.fif.mq.config;

import org.springframework.stereotype.Component;

/**
 * <p>ClassName: FifMqConfig</p>
 * <p>Description: 配置公共类</p>
 * <p>Company: 梦想工厂--基础架构部/p>
 *
 * @author weizhuang
 * @date 2018-05-28
 */
@Component
public class FifMqConfig {

    private String nameServer ;

    private String basePackage;


    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
