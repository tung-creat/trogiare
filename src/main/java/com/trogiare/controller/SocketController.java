//package com.trogiare.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class SocketController {
//    private final SimpMessageSendingOperations messagingTemplate;
//    @Autowired
//    public SocketController(SimpMessageSendingOperations messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//    @MessageMapping("/message")
//    public void onReceivedMessage(String message) {
//        this.messagingTemplate.convertAndSend("/topic/greetings", message);
//    }
//}