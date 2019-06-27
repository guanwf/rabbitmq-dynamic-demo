package com.github.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.Data;

@Data
public class ChannelInfo {
    private Channel channel;
    private String exchange;
    private String queue;
    private String routingKey;
    private String tag;
}
