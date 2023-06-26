package com.trogiare.publisher;

import com.trogiare.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {
    private static final Logger logger= LoggerFactory.getLogger(RabbitMQProducer.class);
//    @RabbitListener(queues = "${rabitmq.queue.name}")
//    public void consume(String message){
//        logger.info(String.format("messsage recive %s",message));
//
//    }
//    private final  String x = "javaguides";
//    @RabbitListener(queues = x)
//    public void consume(UserDto userDto){
//        logger.info(String.format("messsage recive %s",userDto));
//    }
}
