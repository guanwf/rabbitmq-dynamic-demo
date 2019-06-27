package com.github.rabbitmq.consumer;

import com.github.rabbitmq.utils.ChannelUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 消费
 */
public class MessageConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        List<ChannelInfo> channelList=new ArrayList<>();

        //MQ1
        ChannelInfo channelInfo=new ChannelInfo();
        Channel channel = ChannelUtils.getChannelInstance("rabbitmq-sz-队列消息消费者");
        channelInfo.setChannel(channel);
        channelInfo.setExchange("rabbitmq-sz");
        channelInfo.setQueue("rabbitmq-sz-queue");
        channelInfo.setRoutingKey("rabbitmq-sz-queue-key");
        channelInfo.setTag("rabbitmq-sz-MQ队列");
        channelList.add(channelInfo);

        //MQ2
        ChannelInfo channelInfo2=new ChannelInfo();
        Channel channel2 = ChannelUtils.getChannelInstance("rabbitmq-wh-队列消息消费者");
        channelInfo2.setChannel(channel2);
        channelInfo2.setExchange("rabbitmq-wh");
        channelInfo2.setQueue("rabbitmq-wh-queue");
        channelInfo2.setRoutingKey("rabbitmq-wh-queue-key");
        channelInfo2.setTag("rabbitmq-wh-MQ队列");
        channelList.add(channelInfo2);

        channelList.forEach((info)->{
            try{
                process(info);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

    }

    public static void process(ChannelInfo info)throws IOException, TimeoutException {
        Channel channel = info.getChannel();
        String exchangeName=info.getExchange();
        String queueName=info.getQueue();
        String routingKey= info.getRoutingKey();
        String consumerTag=info.getTag();

        System.out.println(">>初始化"+consumerTag);

        // 声明队列 (队列名, 是否持久化, 是否排他, 是否自动删除, 队列属性);
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(queueName, true, false, false, new HashMap<>());

        // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());

        // 将队列Binding到交换机上 (队列名, 交换机名, Routing key, 绑定属性);
        channel.queueBind(declareOk.getQueue(), exchangeName, routingKey, new HashMap<>());

        // 消费者订阅消息 监听如上声明的队列 (队列名, 是否自动应答(与消息可靠有关 后续会介绍), 消费者标签, 消费者)
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(consumerTag);
                System.out.println(envelope.toString());
                System.out.println(properties.toString());
                System.out.println("消息内容:" + new String(body));
            }
        };
        channel.basicConsume(declareOk.getQueue(), true, consumerTag, defaultConsumer);

    }

}
