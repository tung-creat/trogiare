package com.trogiare.controller;

import com.trogiare.model.Message;
import com.trogiare.model.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;

public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/notification")
    @SendTo("/notification/all")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getIdRecive(),"/private",message); //
        System.out.println(message.toString());
        return message;
    }
}

// Message.java
