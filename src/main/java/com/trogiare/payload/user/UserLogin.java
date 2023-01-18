package com.trogiare.payload.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {
    @NotBlank(message = "Not blank")
    private String userName;
    @NotBlank(message = "Not blank")
    private String password;
}
