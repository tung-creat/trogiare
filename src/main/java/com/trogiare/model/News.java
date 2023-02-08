package com.trogiare.model;

import com.trogiare.common.enumrate.NewsStatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name="news")
@Data
public class News implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(36)")
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
    @Column(name="content",columnDefinition = "TEXT")
    private String content;
    private LocalDateTime createdTime;
    @Column(nullable = false)
    private String topic;
    @Column(name="status_news")
    @Enumerated(EnumType.STRING)
    private NewsStatusEnum statusNews;

}
