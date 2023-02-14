package com.trogiare.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class NotificationPayload {
    @NotBlank
    private String message;
    private String content;
}
