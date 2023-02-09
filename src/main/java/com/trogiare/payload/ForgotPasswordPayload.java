package com.trogiare.payload;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Setter
@Getter
public class ForgotPasswordPayload {

    @NotBlank(message="Email can not be blank")
    @Email(message="Invalid email address")
    String email;
}
