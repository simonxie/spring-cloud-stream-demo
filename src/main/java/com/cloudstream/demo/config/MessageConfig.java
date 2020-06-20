package com.cloudstream.demo.config;

import com.cloudstream.demo.queue.DelayDemoTopic;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

/**
 * 配置消息的binding
 *
 */
@EnableBinding(value = {DelayDemoTopic.class})
@Component
public class MessageConfig {

}
