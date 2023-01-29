package com.trogiare.model;
import com.trogiare.payload.PostPayload;
import com.trogiare.utils.HandleStringAndNumber;
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
    @Column(name="address_id")
    private String addressId;
    @Column(name ="price_unit")
    private String priceUnit;
    private Long price;
    @Column(name="compact_number")
    private String compactNumber;
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
    private String status;
    private Integer toilet;
    private String furniture;
    @Column(name="created_time",nullable = false)
    private LocalDateTime createdTime;
    @Column(name="updated_time",nullable = false)
    private LocalDateTime updatedTime;
    @Column(name="owner_id",nullable = false)
    private String ownerId;

    public void setInformationFromPayLoad(PostPayload payload){
        this.setArea(payload.getArea());
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
        this.setPriceUnit(payload.getPriceUnit());
        if(payload.getPrice() != null){
            this.setCompactNumber(HandleStringAndNumber.compactNumber(payload.getPrice()));
        }

    }

}
