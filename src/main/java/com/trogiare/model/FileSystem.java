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
@Table(name="file_system",uniqueConstraints = @UniqueConstraint(columnNames = {"id","name","hash"}))
public class FileSystem implements Serializable {
    @Id
    private String id;
    private String hash;
    private String name;
    private String type;
    @Column(name="created_time")
    private LocalDateTime createdTime;
    private String size;
}
