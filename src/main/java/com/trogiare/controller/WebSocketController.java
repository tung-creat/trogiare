package com.trogiare.controller;

import com.trogiare.model.Message;
import com.trogiare.model.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.Date;

public class WebSocketController {

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) {
        return new OutputMessage(message.getFrom(), message.getText(), new Date());
    }
}

// Message.java
