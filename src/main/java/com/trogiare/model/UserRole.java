package com.trogiare.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Data
@Table(name="user_role")
@AllArgsConstructor
@NoArgsConstructor
public class UserRole implements Serializable {
    @Id
    @GeneratedValue(generator = "objectid-generator")
    @GenericGenerator(name = "objectid-generator", strategy = "com.trogiare.common.ObjectIDGenerator")
    @Column(unique = true, nullable = false, length = 24)
    private String id;
    @Column(name="role_name",nullable = false)
    private String roleName;
    @Column(name="user_id",nullable = false)
    private String userId;

}
