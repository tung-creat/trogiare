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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    private String hash;
    @Column(length = 1000)
    private String name;
    private String type;
    @Column(name="created_time")
    private LocalDateTime createdTime;
    private String size;
}
