package com.trogiare.payload.user;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserFormPayload {
    @NotNull(message = "first name not null")
    @NotBlank(message = "first name not blank")
    private String firstName;
    @NotNull(message ="last name is not null")
    @NotBlank(message = "last name is not blank")
    private String lastName;
    @NotNull(message ="username is not null")
    @NotBlank(message = "user name is not blank")
    private String username;
    @Email
    private String email;
    @NotNull(message = "sdt is not null")
    private String sdt;
    private MultipartFile avatar;
}
