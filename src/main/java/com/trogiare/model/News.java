package com.trogiare.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="news")
public class News {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    private String authorId;
    private String title;
    private String metaTitle;
    private String shortDescription;
    private String content;
    private String createdTime;

}
