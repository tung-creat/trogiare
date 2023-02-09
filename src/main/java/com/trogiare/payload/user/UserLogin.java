package com.trogiare.payload.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserLogin {
    @NotBlank(message = "Not blank")
    private String userName;
    @NotBlank(message = "Not blank")
    private String password;
}
