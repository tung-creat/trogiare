package com.trogiare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Districts {
    @Id
    @Column(length = 20)
    private String code;
    private String name;
    @Column(name="name_en")
    private String nameEn;
    @Column(name="full_name")
    private String fullName;
    @Column(name="full_name_en")
    private String fullNameEn;
    @Column(name="codeName")
    private String codeName;
    @Column(name="provinceCode",length = 20)
    private String provinceCode;

}
