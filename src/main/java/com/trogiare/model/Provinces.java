package com.trogiare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provinces {
    @Id
    @Column(length=20)
    private String code;
    private String name;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name="full_name")
    private String fullName;
    @Column(name = "full_name_en")
    private String fullNameEn;
    @Column(name="code_name")
    private String codeName;

}
