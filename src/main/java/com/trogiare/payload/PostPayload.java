package com.trogiare.payload;

import com.trogiare.common.enumrate.PostDirectionHouseEnum;
import com.trogiare.common.enumrate.PostTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PostPayload {
    @NotBlank(message = "name not blank")
    private String name;
    @NotBlank(message = "address not blank")
    private String addressDetails;
    @NotBlank(message = "province not blank")
    private String province;
    @NotBlank(message = "district not blank")
    private String district;
    @NotBlank(message = "village not blank")
    private String village;
    private String priceUnit;
    private Double useableArea;
    private Double landArea;
    private Long price;
    @NotBlank(message = "area is not blank")
    private Double area;
    private Integer bedroom;
    private String description;
    private Double facade;
    private PostDirectionHouseEnum direction;
    private String juridical;
    private Double gateway;
    private Integer numberFloor;
    private Integer toilet;
    private String furniture;
    private PostTypeEnum typePost;
    @NotBlank(message = "image is not blank")
    private MultipartFile image;
    @NotBlank(message = "imagesDetails is not blank")
    private List<MultipartFile> imagesDetails;


}
