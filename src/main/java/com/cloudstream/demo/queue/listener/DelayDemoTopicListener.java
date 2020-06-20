package com.cloudstream.demo.queue.listener;

import com.cloudstream.demo.config.DelayConstant;
import com.cloudstream.demo.queue.DelayDemoTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;


@Component
@Slf4j
public class DelayDemoTopicListener {
    @Autowired
    DelayDemoTopic delayDemoTopic;

    /**
     * 监听延迟消息通道中的消息
     * @param message
     */
    @StreamListener(value = DelayDemoTopic.DELAY_DEMO_CONSUMER)
    public void listener(Message<BigDecimal> message) {
        //获取重试次数
        int retries = (int)message.getHeaders().get(DelayConstant.X_RETRIES_HEADER);
        //获取消息内容
        BigDecimal money = message.getPayload();
        try {
            String now = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
            //模拟：如果金额大于200，则消息无法消费成功；金额如果大于100，则重试3次；如果金额小于100，直接消费成功
            if (money.compareTo(new BigDecimal(200)) == 1){
                throw new RuntimeException(now+":金额超出200，无法交易。");
            }else if (money.compareTo(new BigDecimal(100)) == 1 && retries <= 3) {
                if (retries == 0) {
                    throw new RuntimeException(now+":金额超出100，消费失败，将进入重试。");
                }else {
                    throw new RuntimeException(now+":金额超出100，当前第" + retries + "次重试。");
                }
            }else {
                log.info("消息消费成功！");
            }
        }catch (Exception e) {
            log.error(e.getMessage());
            if (retries < DelayConstant.X_RETRIES_TOTAL){
                //将消息重新塞入队列
                MessageBuilder<BigDecimal> messageBuilder = MessageBuilder.fromMessage(message)
                        //设置消息的延迟时间
                        .setHeader(DelayConstant.X_DELAY_HEADER,DelayConstant.ruleMap.get(retries + 1))
                        //设置消息已经重试的次数
                        .setHeader(DelayConstant.X_RETRIES_HEADER,retries + 1);
                Message<BigDecimal> reMessage = messageBuilder.build();
                //将消息重新发送到延迟队列中
                delayDemoTopic.delayDemoProducer().send(reMessage);
            }else {
                //超过重试次数，做相关处理
                throw new RuntimeException("超过最大重试次数：" + DelayConstant.X_RETRIES_TOTAL);
            }
        }
    }
}
