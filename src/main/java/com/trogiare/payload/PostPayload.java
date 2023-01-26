package com.trogiare.payload;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
@Setter
@Getter
public class PostPayload {
    @NotNull(message = "name is not null")
    private String name;
    @NotNull(message = "address is not null")
    private String address;
    private String priceUnit;
    private Long price;
    @NotNull(message = "area is not null")
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
    @NotNull(message = "image is not null")
    private MultipartFile image;
    private List<MultipartFile> imagesDetails;


}
