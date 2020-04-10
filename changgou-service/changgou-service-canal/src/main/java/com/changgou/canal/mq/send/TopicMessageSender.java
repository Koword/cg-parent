package com.changgou.canal.mq.send;

import com.alibaba.fastjson.JSON;
import entity.Message;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description 消息发送
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 17:27 2020/4/10
 **/
@Component
@AllArgsConstructor
public class TopicMessageSender {

    RabbitTemplate rabbitTemplate;

    /**
     * Topic消息发送
     * @param msg
     */
    public void  sendMessage(Message msg){
        rabbitTemplate.convertAndSend(msg.getExechange(),msg.getRoutekey(), JSON.toJSONString(msg));
    }
}
