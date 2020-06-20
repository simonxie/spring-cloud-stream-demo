package com.cloudstream.demo.config;


import java.util.HashMap;
import java.util.Map;

public class DelayConstant {
    /**
     * 定义当前重试次数
     */
    public static final String X_RETRIES_HEADER = "x-retries";
    /**
     * 定义延迟消息，固定值，该配置放到消息的header中，会开启延迟队列
     */
    public static final String X_DELAY_HEADER = "x-delay";

    /**
     * 定义最多重试次数
     */
    public static final Integer X_RETRIES_TOTAL = 5;

    /**
     * 定义重试规则，毫秒为单位
     */
    public static final Map<Integer,Integer> ruleMap = new HashMap(){{
        put(1,1000);
        put(2,2000);
        put(3,3000);
        put(4,4000);
        put(5,5000);
    }};
}
