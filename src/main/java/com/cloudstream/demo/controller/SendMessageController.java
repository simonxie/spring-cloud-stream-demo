package com.cloudstream.demo.controller;

import com.cloudstream.demo.config.DelayConstant;
import com.cloudstream.demo.queue.DelayDemoTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 发送消息
 */
@RestController
public class SendMessageController {
    @Autowired
    DelayDemoTopic delayDemoTopic;

    @GetMapping("send")
    public Boolean sendMessage(BigDecimal money) throws JsonProcessingException {

        Message<BigDecimal> message = MessageBuilder.withPayload(money)
                //设置消息的延迟时间，首次发送，不设置延迟时间，直接发送
                .setHeader(DelayConstant.X_DELAY_HEADER,0)
                //设置消息已经重试的次数，首次发送，设置为0
                .setHeader(DelayConstant.X_RETRIES_HEADER,0)
                .build();
        return delayDemoTopic.delayDemoProducer().send(message);
    }
}
