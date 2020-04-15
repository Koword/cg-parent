package com.changgou.rabbitmq.listener.item;

import com.alibaba.fastjson.JSON;
import com.changgou.item.feign.PageFeign;
import entity.Message;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description 生成静态页监听者
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 11:02 2020/4/11
 **/
@Component
@RabbitListener(queues = "topic.queue.spu")
@AllArgsConstructor
public class HtmlGeneratListener {

    PageFeign pageFeign;

    @RabbitHandler
    public void getInfo(String msg) {
        // 将数据装换成Message类型
        Message message = JSON.parseObject(msg, Message.class);
        if (message.getCode() == 2) {
            // 审核，生成静态页面
            pageFeign.createHtml(Long.parseLong(message.getContent().toString()));
        }
    }

    @RabbitHandler
    public void delete(String msg) {
        // 将数据转换成Message类型
        Message message = JSON.parseObject(msg, Message.class);
        if (message.getCode() == 3) {
            // 删除对应页面
            pageFeign.deleteHtml(Long.parseLong(message.getContent().toString()));
        }
    }

}
