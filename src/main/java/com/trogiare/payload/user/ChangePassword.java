package com.trogiare.payload.user;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("form change password")
public class ChangePassword {

    @NotBlank(message="Password can not be blank")
    @Pattern(regexp = "\\S+", message = "Password must not contain whitespace")
    @ApiModelProperty(value = "The user's current password", required = true, example = "oldpassword")
    private String password;


    @NotBlank(message = "New password can not be empty")
    @Size(min= 8,max=25,message = "Password allow min = 8, max 25 character")
    @Pattern(regexp = "\\S+", message = "Password must not contain whitespace")
    @ApiModelProperty(value = "The user's new password", required = true, example = "newpassword")
    private String newPassword;

    @NotBlank(message = "New password can not be empty")
    @Size(min= 8,max=25,message = "reType Password allow min = 8, max 25 character")
    @Pattern(regexp = "\\S+", message = "Password must not contain whitespace")
    @ApiModelProperty(value = "The user's reType Password", required = true, example = "newpassword")
    private String reTypePassword;
}
