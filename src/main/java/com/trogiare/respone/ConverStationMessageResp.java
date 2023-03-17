package com.trogiare.respone;

import com.trogiare.model.Message;
import lombok.Data;
import org.hibernate.mapping.Collection;

import java.util.Collections;
import java.util.List;

@Data
public class ConverStationMessageResp {
    private String userId;
    private String fullName;
    private String converStationId;
    private String avatarImage;
    private List<Message> messageList;

    public void setMessageList(List<Message> messageList) {
//        Collections.reverse(messageList);
        this.messageList = messageList;
    }
}
