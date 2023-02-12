package com.trogiare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name="notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
        @Id
        @GeneratedValue(generator = "objectid-generator")
        @GenericGenerator(name = "objectid-generator", strategy = "com.trogiare.common.ObjectIDGenerator")
        @Column(unique = true, nullable = false, length = 24)
        private String id;
        private String receiver;
        private String message;
        private String typeNotification;
        private LocalDateTime timestamp;
        private boolean isRead;


}
