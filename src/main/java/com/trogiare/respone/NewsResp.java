package com.trogiare.respone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.trogiare.common.enumrate.CategoriesNewsEnum;
import com.trogiare.common.enumrate.NewsStatusEnum;
import com.trogiare.dto.NewsDto;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsResp {
    private String id;
    private String authorId;
    private String title;
    private String metaTitle;
    private String shortDescription;
    private String path;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private CategoriesNewsEnum category;
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
        this.category = news.getCategory();
        this.path = news.getPath();
        this.statusNews = news.getStatusNews();

    }
    public NewsResp(NewsDto news){
        this.id = news.getId();
        this.authorId = news.getAuthorId();
        this.title = news.getTitle();
        this.metaTitle = news.getMetaTitle();
        this.shortDescription = news.getShortDescription();
        this.createdTime = news.getCreatedTime();
        this.updatedTime = news.getUpdatedTime();
        this.category = news.getCategory();
        this.statusNews = news.getStatusNews();

    }
}
