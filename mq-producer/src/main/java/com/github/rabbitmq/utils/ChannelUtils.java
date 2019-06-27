package com.github.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ChannelUtils {

    public static Channel getChannelInstance(String connectionDescription) {
        try {
            ConnectionFactory connectionFactory = getConnectionFactory();
            Connection connection = connectionFactory.newConnection(connectionDescription);
            return connection.createChannel();
        } catch (Exception e) {
            throw new RuntimeException("获取Channel连接失败");
        }
    }

    private static ConnectionFactory getConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        // 配置连接信息
        connectionFactory.setHost("172.17.13.101");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("retail");
        connectionFactory.setPassword("oneretail");

        // 网络异常自动连接恢复
        connectionFactory.setAutomaticRecoveryEnabled(true);
        // 每10秒尝试重试连接一次
        connectionFactory.setNetworkRecoveryInterval(10000);

        return connectionFactory;
    }

}
