package com.trogiare.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private String idFrom;
    private String idRecive;
    private String message;
    private LocalDateTime date;
}