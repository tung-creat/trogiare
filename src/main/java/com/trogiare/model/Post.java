package com.trogiare.model;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.trogiare.common.enumrate.PostDirectionHouseEnum;
import com.trogiare.common.enumrate.PostStatusEnum;
import com.trogiare.common.enumrate.PostTypeEnum;
import com.trogiare.common.enumrate.TypeRealEstateEnum;
import com.trogiare.payload.PostPayload;
import com.trogiare.utils.HandleStringAndNumber;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name="post")
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = String.class)
public class Post implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "objectid-generator")
    @GenericGenerator(name = "objectid-generator", strategy = "com.trogiare.common.ObjectIDGenerator")
    @Column(unique = true, nullable = false, length = 24)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(name="address_id",nullable = false)
    private String addressId;
    private Long price;
    @Column(name="compact_number")
    private String compactNumber;
    @Column(name="useable_area")
    private Double useableArea;
    @Column(name="land_area")
    private Double landArea;
    @Column(name="type_real_estate",nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeRealEstateEnum typeRealEstate;
    private Integer bedroom;
    @Lob
    @Column(name="description",columnDefinition = "TEXT")
    private String description;
    private Double facade;
    @Enumerated(EnumType.STRING)
    private PostDirectionHouseEnum direction;
    private String juridical;
    private Double gateway;
    private Integer numberFloor;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatusEnum status;
    private Integer toilet;
    private String furniture;
    @Column(name="created_time",nullable = false)
    private LocalDateTime createdTime;
    @Column(name="updated_time",nullable = false)
    private LocalDateTime updatedTime;
    @Column(name="expiration_date",nullable = false)
    private LocalDateTime expirationDate;
    @Column(name="owner_id",nullable = false)
    private String ownerId;
    @Column(name="type_post")
    @Enumerated(EnumType.STRING)
    private PostTypeEnum typePost;
    @Column(name="post_code",nullable = false)
    private String postCode;



    public void setInformationFromPayLoad(PostPayload payload){
        this.setLandArea(payload.getLandArea());
        this.setUseableArea(payload.getUseableArea());
        this.setBedroom(payload.getBedroom());
        this.setName(payload.getName());
        this.setDescription(payload.getDescription());
        this.setDirection(payload.getDirection());
        this.setFacade(payload.getFacade());
        this.setJuridical(payload.getJuridical());
        this.setFurniture(payload.getFurniture());
        this.setGateway(payload.getGateway());
        this.setNumberFloor(payload.getNumberFloor());
        this.setToilet(payload.getToilet());
        this.setPrice(payload.getPrice());
        this.setTypeRealEstate(payload.getTypeRealEstate());
        if(payload.getPrice() != null){
            this.setCompactNumber(HandleStringAndNumber.compactNumber(payload.getPrice()));
        }
        this.setTypePost(payload.getTypePost());

    }

}
