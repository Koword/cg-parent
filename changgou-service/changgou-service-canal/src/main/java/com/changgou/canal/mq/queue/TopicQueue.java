package com.changgou.canal.mq.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 队列绑定到交换机
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 17:14 2020/4/10
 **/
@Configuration
public class TopicQueue {
    public static final String TOPIC_QUEUE_SPU = "topic.queue.spu";
    public static final String TOPIC_EXCHANGE_SPU = "topic.exchange.spu";


    /**
     * Topic模式 spu变更队列
     * @return
     */
    @Bean
    public Queue topicQueue(){
        return new Queue(TOPIC_QUEUE_SPU);
    }

    /**
     * spu 队列交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE_SPU);
    }

    /**
     * 队列绑定交换机
     * @return
     */
    @Bean
    public Binding topicBinding(){
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(TOPIC_QUEUE_SPU);
    }
}
