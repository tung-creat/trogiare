package com.trogiare.service;

import com.trogiare.model.Notification;
import com.trogiare.payload.NotificationPayload;
import com.trogiare.repo.NotificationRepo;
import com.trogiare.respone.MessageResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepo notificationRepo;
    public MessageResp saveNotification(@RequestBody NotificationPayload payload){
        Notification notification  = new Notification();
        notification.setMessage(payload.getMessage());
        notification.setContent(payload.getContent());
        notificationRepo.save(notification);
        return null;

    }
}
