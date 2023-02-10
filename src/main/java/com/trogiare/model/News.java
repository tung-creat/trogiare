package com.trogiare.model;

import com.trogiare.common.enumrate.NewsStatusEnum;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name="news")
@Data
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
    @Lob
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    @Column(nullable = false)
    private String topic;
    @Column(name="status_news")
    @Enumerated(EnumType.STRING)
    private NewsStatusEnum statusNews;

}
