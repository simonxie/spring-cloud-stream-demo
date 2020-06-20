package com.cloudstream.demo.queue;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 定义延迟消息通道
 */
public interface DelayDemoTopic {
    /**
     * 生产者，与yml文件配置对应
     */
    String DELAY_DEMO_PRODUCER = "delay-demo-producer";
    /**
     * 消费者，与yml文件配置对应
     */
    String DELAY_DEMO_CONSUMER = "delay-demo-consumer";

    /**
     * 定义消息消费者，在@StreamListener监听消息的时候用到
     * @return
     */
    @Input(DELAY_DEMO_CONSUMER)
    SubscribableChannel delayDemoConsumer();

    /**
     * 定义消息发送者，在发送消息的时候用到
     * @return
     */
    @Output(DELAY_DEMO_PRODUCER)
    MessageChannel delayDemoProducer();
}
