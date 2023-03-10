package com.trogiare.payload;


import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
@Getter
@Setter
public class UserPayload {
    @NotBlank(message = "invalid first name")
    private String firstName;
    @NotBlank(message = "invalid last name")
    private String lastName;
    @NotBlank(message = "invalid user name")
    private String userName;
    @NotBlank
    @Email
    private String email;
    @Pattern(regexp = "(^[+]*[(]{0,1}[0-9]{1,3}[)]{0,1}[-\\s\\./0-9]*$)|([\\s]*)", message = "Invalid phone number")
    private String sdt;
    @NotBlank(message = "invalid password")
    private String password;
    private String rePassword;


}
