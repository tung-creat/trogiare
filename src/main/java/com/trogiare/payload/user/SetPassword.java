package com.trogiare.payload.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
@ApiModel(description = "setpassword form")
public class SetPassword {

    @ApiModelProperty(value = "Access token", required = true)
    @NotBlank(message = "Token can not be blank")
    String otp;

    @ApiModelProperty(value = "New password", required = true)
    @NotBlank(message = "Password can not be blank")
    String yourPassword;

    @ApiModelProperty(value = "Confirm new password", required = true)
    @NotBlank(message = "Retype Password can not be blank")
    String retypePassword;



}
