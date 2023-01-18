package com.trogiare.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ForgotPassword {

    @NotBlank(message="Email can not be blank")
    @Email(message="Invalid email address")
    String email;
}
