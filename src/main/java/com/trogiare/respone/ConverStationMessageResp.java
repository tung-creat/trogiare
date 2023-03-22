package com.trogiare.respone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.trogiare.model.Message;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Collection;

import java.util.Collections;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
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
