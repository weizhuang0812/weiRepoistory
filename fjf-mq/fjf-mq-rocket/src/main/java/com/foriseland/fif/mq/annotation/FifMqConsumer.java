package com.foriseland.fif.mq.annotation;

import java.lang.annotation.*;

/**
 * <p>ClassName: FifMqConsumer</p>
 * <p>Description: rocketMq 消费端注解</p>
 * <p>Company: 梦想工厂--基础架构部/p>
 *
 * @author weizhuang
 * @date 2018-05-22
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface FifMqConsumer {

    String nameServer() default "";

    String groupName() default "-1"; //组名

    String topic() default "test"; //topic

    String tags() default "*"; //tags

    int formWhere() default 0; //每次重启拉取消息节点

    int messageModel() default 1; //消费模式
}
