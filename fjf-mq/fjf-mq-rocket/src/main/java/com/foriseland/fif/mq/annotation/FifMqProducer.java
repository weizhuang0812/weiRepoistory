package com.foriseland.fif.mq.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * <p>ClassName: FifMqConsumer</p>
 * <p>Description: rocketMq 生产端注解</p>
 * <p>Company: 梦想工厂--基础架构部/p>
 *
 * @author weizhuang
 * @date 2018-05-22
 */
@Target({ElementType.FIELD,ElementType.TYPE})
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface FifMqProducer {

    String nameServer() default "";

    String groupName() default "-1"; //组名

}
