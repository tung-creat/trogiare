package com.trogiare.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmail {
    @NotBlank(message="Token Name can not be blank")
    String token;
}
