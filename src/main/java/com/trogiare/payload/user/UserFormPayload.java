package com.trogiare.payload.user;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserFormPayload {
    @NotBlank(message = "invalid first name")
    private String firstName;
    @NotBlank(message = "invalid last name")
    private String lastName;
    private MultipartFile avatar;
}
