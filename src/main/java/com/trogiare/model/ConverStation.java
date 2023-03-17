package com.trogiare.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;
@Document(collection = "conversations")
@Data
public class ConverStation {
    @Id
    private String id;
    private List<String> participants;
    private String idLastMessage;
}
