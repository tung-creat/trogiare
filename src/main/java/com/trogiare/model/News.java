package com.trogiare.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.trogiare.common.enumrate.CategoriesNewsEnum;
import com.trogiare.common.enumrate.NewsStatusEnum;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name="news")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = String.class)
public class News implements Serializable {
    @Id
    @GeneratedValue(generator = "objectid-generator")
    @GenericGenerator(name = "objectid-generator", strategy = "com.trogiare.common.ObjectIDGenerator")
    @Column(unique = true, nullable = false, length = 24)
    private String id;
    @Column(name="author_id",nullable = false)
    private String authorId;
    @Column(nullable = false,unique = true)
    private String title;
    @Column(nullable = false,unique = true)
    private String metaTitle;
    @Column(nullable = false,length = 1200)
    private String shortDescription;
    @Column(nullable = false)
    private String path;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    @Enumerated(EnumType.STRING)
    private CategoriesNewsEnum category;
    @Column(name="status_news")
    @Enumerated(EnumType.STRING)
    private NewsStatusEnum statusNews;

}
