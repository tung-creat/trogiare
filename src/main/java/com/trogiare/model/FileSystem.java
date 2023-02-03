package com.trogiare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="file_system")
public class FileSystem implements Serializable {
    private static final long serialVersionUID = -40943944218239318L;
    @Id
    private String id;
    @Column(length = 1000)
    private String path;
    private String type;
    @Column(name="created_time")
    private LocalDateTime createdTime;
    private String size;
}
