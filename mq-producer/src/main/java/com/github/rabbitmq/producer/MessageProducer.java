package com.github.rabbitmq.producer;

import com.github.rabbitmq.utils.ChannelUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * 生产
 */
public class MessageProducer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //MQ1
        Channel channel = ChannelUtils.getChannelInstance("rabbitmq-sz-队列消息生产者");
        // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
        channel.exchangeDeclare("rabbitmq-sz", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());
        // 设置消息属性 发布消息 (交换机名, Routing key, 可靠消息相关属性 后续会介绍, 消息属性, 消息体);
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();
        channel.basicPublish("rabbitmq-sz", "rabbitmq-sz-queue-key", false, basicProperties, "rabbitmq-sz-body中的消息内容！".getBytes());

        //MQ2
        Channel channel2 = ChannelUtils.getChannelInstance("rabbitmq-wh-队列消息生产者");
        channel2.exchangeDeclare("rabbitmq-wh", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());
        AMQP.BasicProperties basicProperties2 = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();
        channel2.basicPublish("rabbitmq-wh", "rabbitmq-wh-queue-key", false, basicProperties2, "rabbitmq-wh-body中的消息内容！".getBytes());

    }

}
