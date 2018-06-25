package com.foriseland.fjf.mq.property;

/**
 * @ClassName: com.ymhj.yjf.mq.property
 * @Description:
 * @author: wangHaiyang
 * @date 16/7/27
 */
public enum FLKafkaOtherProperties {
    /**
     * kafka 请求的毫秒数
     */
    CONSUMER_REQUEST_MILLS,
    /**
     * 消费者分区数量
     */
    CONSUMER_PARTITION_COUNT,
    /**
     * 生产者分区数量
     */
    PRODUCER_PARTITION_COUNT,
    /**
     * 不同的主题消费者的数量
     */
    TOPIC_CONSUMER_COUNT
}
