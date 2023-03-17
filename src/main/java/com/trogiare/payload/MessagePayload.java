package com.trogiare.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
public class MessagePayload {
    @NotNull(message = "content is not blank")
    private String content;
    @NotNull(message = "uidSender is not blank")
    private String uidSender;
    @NotNull(message = "uidRecipient is not blank")
    private String uidRecipient;
}
