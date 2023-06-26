package com.trogiare.publisher;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.trogiare.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQProducer {
    private final Logger logger = LoggerFactory.getLogger(RabbitMQProducer.class);
    @Value("${rabitmq.exchange.name}")
    private String exchange;
    @Value("${rabitmq.routing.key}")
    private String routing_key;
//    @Value("${rabitmq.routing.json.key}")
//    private String routing_json_key;
    private RabbitTemplate rabbitTemplate;
    public RabbitMQProducer (RabbitTemplate rabbitTemplate){
     this.rabbitTemplate = rabbitTemplate;
    }
    public void sendMessage(String message){
        logger.info("messsage send " + message);
        rabbitTemplate.convertAndSend(exchange,routing_key,message);
    }
//    public void sendMessage(UserDto message){
//        logger.info("messsage send " + message);
//        rabbitTemplate.convertAndSend(exchange,routing_json_key,message);
//    }


}
