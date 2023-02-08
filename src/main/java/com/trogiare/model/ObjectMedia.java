package com.trogiare.model;

import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.common.enumrate.ObjectTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="object_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectMedia {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    private String objectId;
    private String objectType;
    private String refType;
    @Column(name="media_id")
    private String mediaId;

}
