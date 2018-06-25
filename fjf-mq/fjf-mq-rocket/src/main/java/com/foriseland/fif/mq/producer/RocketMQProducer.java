package com.foriseland.fif.mq.producer;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * <p>ClassName: RocketMQProducer</p>
 * <p>Description: rocket mq 生产端</p>
 * <p>Company: 梦想工厂--基础架构部/p>
 *
 * @author weizhuang
 * @date 2018-05-22
 */
public class RocketMQProducer {

    private Logger logger = Logger.getLogger(RocketMQProducer.class);

    private DefaultMQProducer sender; //生产者producer

    private String nameServer; //连接地址

    private String groupName; //组名


    public void init() {

        System.out.println("init groupName "+ groupName + "nameServer"+nameServer );
        //实例化生产者
        sender = new DefaultMQProducer(groupName);
        //设置地址
        sender.setNamesrvAddr(nameServer);
        sender.setSendMsgTimeout(100000);
        //不同的生产者 往不同的集群发送消息 实例编号不允许重复
        sender.setInstanceName(UUID.randomUUID().toString());
        try {
            sender.start();
            System.out.println("----------生产端启动成功~！");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        String msg = new String(message.getBody());
        System.out.println("init  topic "+ message.getTopic() + "keys "+message.getKeys()+" body "+msg );
        try {
            sender.send(message, new SendCallback() {
                @Override
                //成功的回调函数
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送消息返回结果 messageId=" + sendResult.getMsgId() + ", status=" + sendResult.getSendStatus());
                    logger.info("发送信息详情 key =" + message.getKeys() + ", topic=" + message.getTopic() + ", tags=" + message.getTags() + ", body=" + message.getBody());
                    logger.info("发送消息返回结果 messageId=" + sendResult.getMsgId() + ", status=" + sendResult.getSendStatus());
                }

                @Override
                //出现异常的回调函数
                public void onException(Throwable e) {
                    e.printStackTrace();
                    System.out.println("send error~！"+e.getStackTrace());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("send error~！"+e.getStackTrace());
            logger.error(e);
        }
    }

    public void send(String topic , String body) {
        Message  message = new Message(topic,body.getBytes());
        send(message);
    }

    public void send(String topic, String tags ,String body) {
        Message  message = new Message(topic,tags,body.getBytes());
        send(message);
    }

    public void send(String topic, String tags,String keys ,String body) {
        Message  message = new Message(topic,tags,keys,body.getBytes());
        send(message);
    }


    public void destroy() {

        try {
            if (sender != null) {
                sender.shutdown();
                logger.info("生产者销毁");
            } else {
                logger.info("未被实例化 不允许销毁");
            }
        } catch (Exception e) {
            logger.error("堆栈错误信息" + e.getStackTrace());
        }
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


}
