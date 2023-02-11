package com.trogiare.dto;

import com.trogiare.common.enumrate.NewsStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private String id;
    private String authorId;
    private String title;
    private String metaTitle;
    private String shortDescription;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String topic;
    private NewsStatusEnum statusNews;
}
