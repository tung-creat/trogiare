package com.trogiare.controller;
import com.trogiare.model.Message;
import com.trogiare.payload.MessagePayload;
import com.trogiare.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@CrossOrigin("*")
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepo messageRepo;



    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message")
    public MessagePayload recMessage(@Payload MessagePayload payload) {
        Message message = new Message();
        message.setTimestamp(LocalDateTime.now());
        message.setUidSender(payload.getUidSender());
        message.setContent(payload.getContent());
        message.setUidRecipient(payload.getUidRecipient());
        messageRepo.save(message);
        simpMessagingTemplate.convertAndSendToUser(payload.getUidRecipient(), "/private", message);
        return payload;
    }
}

// Message.java
