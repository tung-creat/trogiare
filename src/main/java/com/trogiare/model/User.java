package com.trogiare.model;

import javax.persistence.*;
import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="user",uniqueConstraints = @UniqueConstraint(columnNames = {"email","sdt"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(generator = "objectid-generator")
    @GenericGenerator(name = "objectid-generator", strategy = "com.trogiare.common.ObjectIDGenerator")
    @Column(unique = true, nullable = false, length = 24)
    private String id;
    @Column(name="first_name",nullable = false)
    private String firstName;
    @Column(name="last_name",nullable = false)
    private String lastName;
    @Column(name="username", nullable = false)
    private String username;
    @Column(nullable = false)
    @Email
    private String email;
    private String sdt;
    private String password;
    private String status;
    private String avatar;
    @Column(name ="created_time",nullable = false)
    private LocalDateTime createdTime;
    @Column(name="updated_time",nullable = false)
    private LocalDateTime updatedTime;
}
