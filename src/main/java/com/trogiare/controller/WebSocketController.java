package com.trogiare.controller;

import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.trogiare.component.ConverstationComponent;
import com.trogiare.model.ConverStation;
import com.trogiare.model.Message;
import com.trogiare.payload.MessagePayload;
import com.trogiare.repo.ConverStationRepo;
import com.trogiare.repo.MessageRepo;
import com.trogiare.utils.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin("*")
public class WebSocketController {
    static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private ConverstationComponent converstationComponent;
    @Autowired
    private ConverStationRepo converStationRepo;



    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message")
    public MessagePayload recMessage(@Payload MessagePayload payload) {
        logger.info("pay loadd {}",payload);
        Message message = new Message();
        message.setId(IdUtil.generate());
        List<String> listPraList = new ArrayList<>();
        listPraList.add(payload.getUidRecipient());
        listPraList.add(payload.getUidSender());
        Collections.sort(listPraList);
        ConverStation converStation =  converstationComponent.getConverStation(listPraList);
        if (converStation == null) {
            converStation = new ConverStation();
            converStation.setParticipants(listPraList);
            converStation.setIdLastMessage(message.getId());
            converStation = converStationRepo.save(converStation);
            message.setTimestamp(LocalDateTime.now());
            message.setUidSender(payload.getUidSender());
            message.setContent(payload.getContent());
            message.setConversationId(converStation.getId());
            message.setUidRecipient(payload.getUidRecipient());
            messageRepo.save(message);
        } else {
            converStation.setIdLastMessage(message.getId());
            message.setTimestamp(LocalDateTime.now());
            message.setUidSender(payload.getUidSender());
            message.setContent(payload.getContent());
            message.setConversationId(converStation.getId());
            message.setUidRecipient(payload.getUidRecipient());
            messageRepo.save(message);
            converStationRepo.save(converStation);
        }

        simpMessagingTemplate.convertAndSendToUser(payload.getUidRecipient(), "/private", message);
        return payload;
    }
}

// Message.java
