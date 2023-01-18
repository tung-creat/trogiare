package com.trogiare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="user_token")
@Getter
@Setter
public class UserToken implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    @Column(name="user_id",nullable = false)
    private String userId;
    @Column(name ="token",nullable = false)
    private String token;
    @Column(name="token_type",nullable = false)
    private String tokenType;
    @Column(name="status",nullable = false)
    private String status;
    @Column(name="created_time",nullable = false)
    private LocalDateTime createdTime;
    @Column(name="expired_time",nullable = false)
    private LocalDateTime expiredTime;
}
