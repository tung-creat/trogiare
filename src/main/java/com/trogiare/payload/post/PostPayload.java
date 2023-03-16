package com.trogiare.payload.post;

import com.trogiare.common.enumrate.PostDirectionHouseEnum;
import com.trogiare.common.enumrate.PostTypeEnum;
import com.trogiare.common.enumrate.TypeRealEstateEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PostPayload {
    private String postId;
    @NotBlank(message = "name not blank")
    @Size(max=500,message = "allow name post max = 500 character ")
    private String name;
    @NotBlank(message = "address not blank")
    private String addressDetails;
    @NotBlank(message = "province not blank")
    private String province;
    @NotBlank(message = "district not blank")
    private String district;
    @NotBlank(message = "village not blank")
    private String village;
    @NotNull(message = "useable Are is not blank")
    private Double useableArea;
    private Double landArea;
    private Long price;
    @NotNull(message = "type real_estate is not blank")
    private TypeRealEstateEnum typeRealEstate;
    private Integer bedroom;
    private String description;
    private Double facade;
    private PostDirectionHouseEnum direction;
    private String juridical;
    private Double gateway;
    private Integer numberFloor;
    private Integer toilet;
    private String furniture;
    @NotNull(message = "type post can't blank")
    private PostTypeEnum typePost;
    private MultipartFile image;
    @Size(max=5 , message = "images details allow max 5 file")
    private List<MultipartFile> imagesDetails;
    private List<String>  imageDelete;


}
