package com.trogiare.payload.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassword {
    @NotNull(message = "password is not null")
    @NotBlank(message = "password is not blank")
    String password;
    @NotNull(message = "repassword is not null")
    @NotBlank(message = "repassword is not blank")
    String rePassword;
}
