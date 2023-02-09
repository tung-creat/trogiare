package com.trogiare.payload;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class VerifyEmail {
    @NotBlank(message="Token Name can not be blank")
    String token;
}
