package com.trogiare.respone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.trogiare.model.Address;
import com.trogiare.model.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
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
    private String priceUnit;
    private Long price;
    private String compactNumber;
    private Double area;
    private Integer bedroom;
    private String description;
    private Double facade;
    private String direction;
    private String juridical;
    private Double gateway;
    private Integer numberFloor;
    private Integer toilet;
    private String furniture;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String ownerId;
    private Address address;
    private String image;
    private List<String> imageDetails;
    public void setPost(Post post){
        this.id = post.getId();
        this.name = post.getName();
        this.priceUnit = post.getPriceUnit();
        this.price = post.getPrice();
        this.compactNumber = post.getCompactNumber();
        this.area = post.getArea();
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
    }
}
