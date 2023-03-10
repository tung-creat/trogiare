package com.trogiare.respone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.trogiare.common.enumrate.PostDirectionHouseEnum;
import com.trogiare.common.enumrate.PostTypeEnum;
import com.trogiare.common.enumrate.TypeRealEstateEnum;
import com.trogiare.model.Address;
import com.trogiare.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
@JsonIgnoreProperties
public class PostResp {
    private String id;
    private String name;
    private Long price;
    private String compactNumber;
    private Integer bedroom;
    private String description;
    private Double facade;
    private PostDirectionHouseEnum direction;
    private String juridical;
    private Double gateway;
    private Integer numberFloor;
    private Integer toilet;
    private String furniture;
    private String typeRealEstate;
    private LocalDateTime createdTime;
    private LocalDateTime expirationDate;
    private String ownerId;
    private PostTypeEnum typePost;
    private String postCode;

    private Double useableArea;

    private Double landArea;
    private LocalDateTime updatedTime;
    private Address address;
    private String image;
    private List<String> imageDetails;
    private UserResp user;

    public void setPost(Post post) {
        this.id = post.getId();
        this.name = post.getName();
        this.price = post.getPrice();
        this.compactNumber = post.getCompactNumber();
        this.bedroom = post.getBedroom();
        this.description = post.getDescription();
        this.facade = post.getFacade();
        this.direction = post.getDirection();
        this.juridical = post.getJuridical();
        this.gateway = post.getGateway();
        this.numberFloor = post.getNumberFloor();
        this.toilet = post.getToilet();
        this.furniture = post.getFurniture();
        this.createdTime = post.getCreatedTime();
        this.updatedTime = post.getUpdatedTime();
        this.ownerId = post.getOwnerId();
        this.expirationDate = post.getExpirationDate();
        this.postCode = post.getPostCode();
        this.typePost = post.getTypePost();
        this.landArea = post.getLandArea();
        this.useableArea = post.getUseableArea();
        this.typeRealEstate = post.getTypeRealEstate();
    }
}
