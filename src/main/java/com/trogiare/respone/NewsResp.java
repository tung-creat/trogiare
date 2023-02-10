package com.trogiare.respone;

import com.trogiare.common.enumrate.NewsStatusEnum;
import com.trogiare.model.News;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NewsResp {
    private String id;
    private String authorId;
    private String title;
    private String metaTitle;
    private String shortDescription;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String topic;
    private NewsStatusEnum statusNews;
    String imageAvatar;
    public NewsResp(News news){
        this.id = news.getId();
        this.authorId = news.getAuthorId();
        this.title = news.getTitle();
        this.metaTitle = news.getMetaTitle();
        this.shortDescription = news.getShortDescription();
        this.createdTime = news.getCreatedTime();
        this.updatedTime = news.getUpdatedTime();
        this.topic = news.getTopic();
        this.statusNews = news.getStatusNews();

    }
}
