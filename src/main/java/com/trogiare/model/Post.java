package com.trogiare.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name="post")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Post implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    private String name;
    private String address;
    @Column(name ="price_unit")
    private String priceUnit;
    private Long price;
    private Double area;
    private Integer bedroom;
    @Lob
    @Column(name="description",columnDefinition = "TEXT")
    private String description;
    private Double facade;
    private String direction;
    private String juridical;
    private Double gateway;
    private Integer numberFloor;
    private Integer toilet;
    private String furniture;
    private String coordinates;
    @Column(name="created_time",nullable = false)
    private LocalDateTime createdTime;
    @Column(name="updated_time",nullable = false)
    private LocalDateTime updatedTime;
    @Column(name="priority_level")
    private Integer priorityLevel;

}
