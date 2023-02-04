package com.trogiare.payload.news;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class NewsPayload {
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
    @NotNull(message = "favicon not null")
    private MultipartFile favicon;
    @NotNull(message = "image Avatar not null")
    private MultipartFile imageAvatar;
}
