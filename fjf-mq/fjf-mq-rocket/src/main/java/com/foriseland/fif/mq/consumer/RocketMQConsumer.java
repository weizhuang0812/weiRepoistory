package com.foriseland.fif.mq.consumer;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListener;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * <p>ClassName: RocketMQConsumer</p>
 * <p>Description: rocket mq 消费端</p>
 * <p>Company: 梦想工厂--基础架构部/p>
 *
 * @author weizhuang
 * @date 2018-05-22
 */
public class RocketMQConsumer {

    private Logger logger = Logger.getLogger(RocketMQConsumer.class);

    private DefaultMQPushConsumer consumer; //消费端实例

    private MessageListener listener ; //消费监听实例

    private String nameServer; //连接地址

    private String groupName; //组名 消费生产对应组名要求一致

    private String topic; //topic

    private String tags; //tags

    private Integer formWhere; //每次重启拉取消息节点

    private Integer messageModel; //消费模式


    public RocketMQConsumer(){
    }

    public void init() {
        consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(nameServer);
        consumer.setClientCallbackExecutorThreads(10);
        consumer.setPollNameServerInteval(30000);
        consumer.setHeartbeatBrokerInterval(30000);
        consumer.setPersistConsumerOffsetInterval(10000);
        consumer.setConsumeMessageBatchMaxSize(100);
        try {
            consumer.subscribe(topic, tags);
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        consumer.setInstanceName(UUID.randomUUID().toString());
        //根据不同的业务需求 拉取节点可控
        if(formWhere == null || formWhere == 0){
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        }
        if(formWhere != null && formWhere == 1){
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        }
        //根据不同的配置 设置不同的消费模式
        if(messageModel == null || messageModel == 0){
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }else{
            consumer.setMessageModel(MessageModel.CLUSTERING);
        }
        consumer.registerMessageListener(listener);

        try{
            consumer.start();
            System.out.println("启动成功");
        }catch (Exception e){
            System.out.printf(e.getMessage());
        }
        logger.info("RocketMQConsumer Started! group=" + consumer.getConsumerGroup() + " instance=" + consumer.getInstanceName());
    }

    public void destroy() {
        try {
            if (consumer != null) {
                consumer.shutdown();
                logger.info("消费者销毁");
            } else {
                logger.info("未被实例化 不允许销毁");
            }
        } catch (Exception e) {
            logger.error("堆栈错误信息" + e.getStackTrace());
        }
    }


    public MessageListener getListener() {
        return listener;
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getFormWhere() {
        return formWhere;
    }

    public void setFormWhere(Integer formWhere) {
        this.formWhere = formWhere;
    }

    public Integer getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(Integer messageModel) {
        this.messageModel = messageModel;
    }
}
