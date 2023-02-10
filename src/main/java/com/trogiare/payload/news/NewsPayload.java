package com.trogiare.payload.news;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewsPayload {
    private String id;
    @NotBlank(message ="title not blank")
    private String title;
    @NotBlank(message = "metaTitle not blank")
    private String metaTitle;
    @NotBlank(message = "short description not blank")
    private String shortDescription;
    @NotBlank(message = "content not blank")
    private String content;
    @NotBlank(message = "topic not blank")
    private String topic;
    private MultipartFile imageAvatar;
    private String linkImageBeDelete;

}
