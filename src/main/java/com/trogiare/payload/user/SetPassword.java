package com.trogiare.payload.user;

import javax.validation.constraints.NotBlank;

public class SetPassword {
    @NotBlank(message="email can not be blank")
    String email;

    @NotBlank(message="Token can not be blank")
    String token;

    @NotBlank(message="Password can not be blank")
    String yourPassword;

    @NotBlank(message="Retype Password can not be blank")
    String retypePassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getYourPassword() {
        return yourPassword;
    }

    public String getRetypePassword() {
        return retypePassword;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public void setYourPassword(String yourPassword) {
        this.yourPassword = yourPassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }

}
