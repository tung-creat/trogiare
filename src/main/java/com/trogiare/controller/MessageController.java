package com.trogiare.controller;

import com.trogiare.dto.UserDto;
import com.trogiare.publisher.RabbitMQProducer;
import com.trogiare.respone.MessageResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
    final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @GetMapping("/message")
    public HttpEntity<?> sendMessage(@RequestParam String content){
        rabbitMQProducer.sendMessage(content);
        return ResponseEntity.ok().body("Message is sent");
    }
//    @PostMapping("")
//    public HttpEntity<?> sendMessage(@RequestBody UserDto userDto){
//        logger.info("Message recive " + userDto);
//        rabbitMQProducer.sendMessage(userDto);
//        return ResponseEntity.ok().body(MessageResp.ok());
//    }

}
